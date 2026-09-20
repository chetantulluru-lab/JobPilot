import os
import shutil
import subprocess
import sys

BASE_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
DESKTOP_DIST = os.path.join(BASE_DIR, "desktop_dist")
PUBLIC_DOWNLOADS = os.path.join(BASE_DIR, "public", "downloads")
DIST_DOWNLOADS = os.path.join(BASE_DIR, "dist", "downloads")

print("=== 1. Building Vite Frontend Assets ===")
subprocess.check_call(["npm.cmd", "run", "build"], cwd=BASE_DIR)

print("\n=== 2. Compiling Windows Standalone Bundle (PyInstaller) ===")
pyinstaller_cmd = [
    sys.executable,
    "-m",
    "PyInstaller",
    "--noconfirm",
    "--onedir",
    "--windowed",
    "--icon",
    os.path.join(BASE_DIR, "desktop", "icon.ico"),
    "--name",
    "JobPilot",
    "--distpath",
    DESKTOP_DIST,
    "--workpath",
    os.path.join(BASE_DIR, "build_desktop"),
    "--add-data",
    f"{os.path.join(BASE_DIR, 'dist', 'index.html')};dist",
    "--add-data",
    f"{os.path.join(BASE_DIR, 'dist', 'assets')};dist/assets",
    "--add-data",
    f"{os.path.join(BASE_DIR, 'dist', 'favicon.svg')};dist",
    "--add-data",
    f"{os.path.join(BASE_DIR, 'dist', 'icons.svg')};dist",
    os.path.join(BASE_DIR, "desktop", "main.py"),
]
subprocess.check_call(pyinstaller_cmd, cwd=BASE_DIR)

print("\n=== 3. Compiling Windows Setup Installer (Inno Setup 6) ===")
iscc_path = r"C:\Users\chetan\AppData\Local\Programs\Inno Setup 6\ISCC.exe"
if not os.path.exists(iscc_path):
    iscc_path = "ISCC.exe"

iss_script = os.path.join(BASE_DIR, "desktop", "installer.iss")
subprocess.check_call([iscc_path, iss_script], cwd=BASE_DIR)

print("\n=== 4. Syncing Setup Installer to Website Downloads ===")
os.makedirs(PUBLIC_DOWNLOADS, exist_ok=True)
os.makedirs(DIST_DOWNLOADS, exist_ok=True)

setup_src = os.path.join(DESKTOP_DIST, "installer", "JobPilot-Setup.exe")
if os.path.exists(setup_src):
    shutil.copy2(setup_src, os.path.join(PUBLIC_DOWNLOADS, "JobPilot-Setup.exe"))
    shutil.copy2(setup_src, os.path.join(DIST_DOWNLOADS, "JobPilot-Setup.exe"))
    size_mb = os.path.getsize(setup_src) / (1024 * 1024)
    print(f"\n==========================================")
    print(f"SUCCESS: JobPilot-Setup.exe created: {size_mb:.2f} MB")
    print(f"==========================================")
else:
    print("ERROR: JobPilot-Setup.exe not found at", setup_src)
    sys.exit(1)
