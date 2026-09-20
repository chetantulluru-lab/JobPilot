import os
import sys
import threading
import http.server
import socketserver
import socket
import webview

def get_dist_dir():
    if getattr(sys, 'frozen', False):
        # Bundled executable via PyInstaller
        base_path = getattr(sys, '_MEIPASS', os.path.dirname(sys.executable))
        return os.path.join(base_path, 'dist')
    else:
        # Development mode
        return os.path.abspath(os.path.join(os.path.dirname(__file__), '..', 'dist'))

dist_dir = get_dist_dir()

def get_free_port():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.bind(('127.0.0.1', 0))
        return s.getsockname()[1]

port = get_free_port()

class SPAHandler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=dist_dir, **kwargs)

    def do_GET(self):
        # Resolve requested file path
        path = self.translate_path(self.path)
        if not os.path.exists(path) or (os.path.isdir(path) and not os.path.exists(os.path.join(path, "index.html"))):
            # Rewrite to SPA index.html
            self.path = "/index.html"
        return super().do_GET()

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
        background_color='#0B0F19'
    )
    webview.start()

if __name__ == '__main__':
    main()
