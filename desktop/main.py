import os
import sys
import threading
import http.server
import socketserver
import socket
import json
import urllib.request
import urllib.error
import webview

def get_dist_dir():
    if getattr(sys, 'frozen', False):
        exe_dir = os.path.dirname(sys.executable)
        meipass = getattr(sys, '_MEIPASS', exe_dir)
        candidates = [
            os.path.join(meipass, 'dist'),
            os.path.join(exe_dir, '_internal', 'dist'),
            os.path.join(exe_dir, 'dist'),
            meipass,
        ]
        for c in candidates:
            if os.path.exists(os.path.join(c, 'index.html')):
                return c
        return candidates[0]
    else:
        return os.path.abspath(os.path.join(os.path.dirname(__file__), '..', 'dist'))

dist_dir = get_dist_dir()

def get_free_port():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind(('127.0.0.1', 0))
        return s.getsockname()[1]

port = get_free_port()

RENDER_BACKEND = "https://jobpilot-backend-e97f.onrender.com"

class SPAHandler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=dist_dir, **kwargs)

    def do_proxy(self):
        target_url = f"{RENDER_BACKEND}{self.path}"
        content_len = int(self.headers.get("Content-Length", 0))
        body = self.rfile.read(content_len) if content_len > 0 else None

        req_headers = {}
        for k, v in self.headers.items():
            if k.lower() not in ["host", "origin", "referer", "connection"]:
                req_headers[k] = v
        req_headers["User-Agent"] = "JobPilot-Windows-Desktop/1.0"

        req = urllib.request.Request(target_url, data=body, headers=req_headers, method=self.command)
        try:
            with urllib.request.urlopen(req, timeout=35) as resp:
                resp_body = resp.read()
                self.send_response(resp.status)
                for hk, hv in resp.headers.items():
                    if hk.lower() not in ["transfer-encoding", "content-encoding", "connection"]:
                        self.send_header(hk, hv)
                self.end_headers()
                self.wfile.write(resp_body)
        except urllib.error.HTTPError as e:
            err_body = e.read()
            self.send_response(e.code)
            for hk, hv in e.headers.items():
                if hk.lower() not in ["transfer-encoding", "content-encoding", "connection"]:
                    self.send_header(hk, hv)
            self.end_headers()
            self.wfile.write(err_body)
        except Exception as e:
            self.send_response(502)
            self.send_header("Content-Type", "application/json")
            self.end_headers()
            self.wfile.write(json.dumps({"detail": f"Proxy Error: {str(e)}"}).encode("utf-8"))

    def do_GET(self):
        if self.path.startswith("/api/"):
            return self.do_proxy()
        # Resolve requested file path
        path = self.translate_path(self.path)
        if not os.path.exists(path) or (os.path.isdir(path) and not os.path.exists(os.path.join(path, "index.html"))):
            # Rewrite to SPA index.html
            self.path = "/index.html"
        return super().do_GET()

    def do_POST(self):
        if self.path.startswith("/api/"):
            return self.do_proxy()
        self.send_error(404)

    def do_PUT(self):
        if self.path.startswith("/api/"):
            return self.do_proxy()
        self.send_error(404)

    def do_DELETE(self):
        if self.path.startswith("/api/"):
            return self.do_proxy()
        self.send_error(404)

    def do_PATCH(self):
        if self.path.startswith("/api/"):
            return self.do_proxy()
        self.send_error(404)

    def do_OPTIONS(self):
        if self.path.startswith("/api/"):
            return self.do_proxy()
        self.send_response(200)
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS")
        self.send_header("Access-Control-Allow-Headers", "*")
        self.end_headers()

    def log_message(self, format, *args):
        # Suppress noisy HTTP request logging
        pass

def run_server():
    socketserver.TCPServer.allow_reuse_address = True
    with socketserver.TCPServer(('127.0.0.1', port), SPAHandler) as httpd:
        httpd.serve_forever()

def main():
    # Start local web server in daemon thread
    server_thread = threading.Thread(target=run_server, daemon=True)
    server_thread.start()

    # Launch native desktop window (Edge Chromium / WebView2)
    window = webview.create_window(
        title='JobPilot - AI Career & Interview Copilot (Desktop App)',
        url=f'http://127.0.0.1:{port}/app',
        width=1280,
        height=840,
        min_size=(960, 600),
        confirm_close=False,
        background_color='#FFFDFB'
    )
    webview.start()

if __name__ == '__main__':
    main()
