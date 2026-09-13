# 🚀 JobPilot Deployment & Operations Guide

---

## 1. Local Development Environment

### Prerequisites
- Python 3.11+ (virtual environment configured in `backend/.venv`)
- PostgreSQL 17 running locally on `localhost:5432`
- Node.js 20+ (for website)
- Android Studio Iguana / Jellyfish with Android SDK 34 and JDK 17

### 1.1 Start Database & Run Migrations
```powershell
cd C:\Users\chetan\OneDrive\Desktop\JobPilot\backend
.\.venv\Scripts\Activate.ps1
alembic upgrade head
```

### 1.2 Start FastAPI Backend
```powershell
uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
```
API Documentation is live at `http://localhost:8000/docs`.

### 1.3 Start Public Marketing Website
```powershell
cd C:\Users\chetan\OneDrive\Desktop\JobPilot
npm run dev
```
Public site is live at `http://localhost:5173`.

### 1.4 Build & Run Android App
```powershell
cd C:\Users\chetan\OneDrive\Desktop\JobPilot\android
.\gradlew.bat assembleDebug
```
The resulting APK is generated at:  
`android/app/build/outputs/apk/debug/app-debug.apk`.

Install to connected emulator/device:
```powershell
adb install app\build\outputs\apk\debug\app-debug.apk
```

---

## 2. Docker & Container Deployment

A production container configuration for the backend:

### Dockerfile (`backend/Dockerfile`)
```dockerfile
FROM python:3.11-slim

WORKDIR /app

# Install system libraries for PDF extraction & rendering
RUN apt-get update && apt-get install -y --no-install-recommends \
    gcc libpq-dev \
    && rm -rf /var/lib/apt/lists/*

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8000

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000", "--workers", "4"]
```

### Docker Compose (`docker-compose.yml`)
```yaml
version: '3.8'

services:
  db:
    image: postgres:17
    restart: always
    environment:
      POSTGRES_USER: ${POSTGRES_USER:-jobpilot_admin}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_DB: ${POSTGRES_DB:-jobpilot}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

  backend:
    build: ./backend
    restart: always
    depends_on:
      - db
    env_file:
      - ./backend/.env
    ports:
      - "8000:8000"

volumes:
  postgres_data:
```

---

## 3. Production Environment Checklist

- [ ] `ENVIRONMENT=production` in backend `.env`.
- [ ] Strong random `SECRET_KEY` generated (`openssl rand -hex 32`).
- [ ] Reverse proxy (Nginx / Cloudflare) terminating TLS with HTTPS.
- [ ] CORS origins restricted to production domain in `app/main.py`.
- [ ] Database automated backups enabled.
- [ ] OpenRouter / DeepSeek API usage alerts set in provider console.
