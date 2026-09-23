import os
import base64

upload_dir = r"C:\Users\chetan\.gemini\antigravity\brain\fdd3faad-af8c-41b2-a33d-39b822123ff4\.user_uploaded"
base_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))

def get_b64(fname):
    p = os.path.join(upload_dir, fname)
    if os.path.exists(p):
        with open(p, "rb") as f:
            ext = "png" if fname.endswith(".png") else "jpeg"
            return f"data:image/{ext};base64,{base64.b64encode(f.read()).decode('utf-8')}"
    return ""

img_mob1 = get_b64("media_1789363134256.jpg")
img_mob2 = get_b64("media_1789363134449.jpg")
img_mob3 = get_b64("media_1789380349316.jpg")
img_mob4 = get_b64("media_1789403199429.png")
img_desk1 = get_b64("media_1789892967611.png")
img_desk2 = get_b64("media_1789916458446.png")

html_content = f"""<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>JobPilot — Project Presentation Deck</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800;900&family=Space+Grotesk:wght@700;800&display=swap" rel="stylesheet">
  <style>
    :root {{
      --orange: #FF6A00;
      --orange-hover: #E65500;
      --orange-light: #FFF5EC;
      --slate-900: #0F172A;
      --slate-800: #1E293B;
      --slate-700: #334155;
      --slate-500: #64748B;
      --slate-200: #E2E8F0;
      --slate-100: #F1F5F9;
      --bg: #0B0F19;
      --card-bg: #FFFFFF;
      --green: #10B981;
      --blue: #0284C7;
      --purple: #8B5CF6;
    }}

    * {{
      box-sizing: border-box;
      margin: 0;
      padding: 0;
      user-select: none;
    }}

    body {{
      background: var(--bg);
      font-family: 'Plus Jakarta Sans', -apple-system, sans-serif;
      color: var(--slate-900);
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      overflow: hidden;
    }}

    /* Presentation Viewport */
    .deck-container {{
      width: 100vw;
      height: 100vh;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      position: relative;
    }}

    .slide-wrapper {{
      width: min(94vw, 1360px);
      height: min(88vh, 765px);
      aspect-ratio: 16 / 9;
      background: #FFFDFB;
      border-radius: 20px;
      box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5), 0 0 0 1px rgba(255, 255, 255, 0.1);
      position: relative;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      padding: 38px 46px;
    }}

    .slide {{
      display: none;
      width: 100%;
      height: 100%;
      flex-direction: column;
      animation: fadeIn 0.3s ease;
    }}

    .slide.active {{
      display: flex;
    }}

    @keyframes fadeIn {{
      from {{ opacity: 0; transform: translateY(6px); }}
      to {{ opacity: 1; transform: translateY(0); }}
    }}

    /* Header */
    .slide-header {{
      margin-bottom: 20px;
      position: relative;
    }}

    .slide-accent-bar {{
      width: 54px;
      height: 4px;
      background: var(--orange);
      border-radius: 2px;
      margin-bottom: 10px;
    }}

    .slide-title {{
      font-size: 1.65rem;
      font-weight: 800;
      color: var(--slate-900);
      letter-spacing: -0.02em;
    }}

    .slide-subtitle {{
      font-size: 0.875rem;
      color: var(--slate-500);
      margin-top: 4px;
    }}

    /* Controls Bar */
    .controls-bar {{
      position: absolute;
      bottom: 18px;
      display: flex;
      align-items: center;
      gap: 16px;
      background: rgba(15, 23, 42, 0.85);
      backdrop-filter: blur(12px);
      padding: 8px 18px;
      border-radius: 30px;
      border: 1px solid rgba(255, 255, 255, 0.12);
      z-index: 100;
    }}

    .nav-btn {{
      background: transparent;
      border: none;
      color: #FFFFFF;
      font-size: 1.1rem;
      cursor: pointer;
      padding: 4px 10px;
      border-radius: 8px;
      transition: all 0.2s ease;
    }}

    .nav-btn:hover:not(:disabled) {{
      background: var(--orange);
    }}

    .nav-btn:disabled {{
      opacity: 0.3;
      cursor: not-allowed;
    }}

    .slide-counter {{
      font-size: 0.8125rem;
      font-weight: 700;
      color: #CBD5E1;
      letter-spacing: 0.05em;
    }}

    /* Card Components */
    .grid-2 {{
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 20px;
      flex: 1;
    }}

    .grid-3 {{
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 18px;
      flex: 1;
    }}

    .grid-4 {{
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 16px;
      flex: 1;
    }}

    .card {{
      background: #FFFFFF;
      border: 1.5px solid var(--slate-200);
      border-radius: 16px;
      padding: 20px;
      display: flex;
      flex-direction: column;
      box-shadow: 0 4px 12px rgba(15, 23, 42, 0.02);
    }}

    .card-orange {{
      background: var(--orange-light);
      border-color: var(--orange);
    }}

    .card-blue {{
      background: #F0F9FF;
      border-color: var(--blue);
    }}

    .card-purple {{
      background: #FAF5FF;
      border-color: var(--purple);
    }}

    .card-title {{
      font-size: 1.05rem;
      font-weight: 800;
      margin-bottom: 12px;
      display: flex;
      align-items: center;
      gap: 8px;
    }}

    .point-item {{
      font-size: 0.8125rem;
      line-height: 1.5;
      margin-bottom: 10px;
      color: var(--slate-700);
    }}

    .point-item strong {{
      color: var(--slate-900);
    }}

    .nlp-box {{
      background: #FFFFFF;
      border: 1.5px solid var(--slate-200);
      border-radius: 12px;
      padding: 12px 16px;
      margin-bottom: 10px;
    }}

    .nlp-box-title {{
      font-size: 0.875rem;
      font-weight: 800;
      color: var(--slate-900);
      display: flex;
      justify-content: space-between;
    }}

    .nlp-box-desc {{
      font-size: 0.75rem;
      color: var(--slate-700);
      margin-top: 4px;
      line-height: 1.4;
    }}

    .img-preview {{
      width: 100%;
      height: 90%;
      object-fit: contain;
      border-radius: 12px;
      box-shadow: 0 4px 14px rgba(15, 23, 42, 0.1);
      background: #FFFFFF;
    }}

    .caption {{
      text-align: center;
      font-size: 0.75rem;
      font-weight: 700;
      color: var(--slate-700);
      margin-top: 6px;
    }}

    .badge {{
      display: inline-block;
      padding: 4px 10px;
      border-radius: 20px;
      font-size: 0.6875rem;
      font-weight: 800;
      background: var(--orange-light);
      color: var(--orange);
    }}
  </style>
</head>
<body>

  <div class="deck-container">
    <div class="slide-wrapper">
      
      <!-- ====================================================================
           SLIDE 1: TITLE
           ==================================================================== -->
      <div class="slide active" id="slide-1">
        <div style="flex: 1; display: flex; flex-direction: column; justify-content: center; padding: 20px 30px;">
          <div style="display: flex; align-items: center; gap: 8px; margin-bottom: 12px;">
            <span class="badge">✦ AI-POWERED CAREER PLATFORM</span>
            <span class="badge" style="background: #F0F9FF; color: var(--blue);">ANDROID APK + WINDOWS DESKTOP</span>
          </div>
          
          <h1 style="font-family: 'Space Grotesk', sans-serif; font-size: 3.5rem; font-weight: 800; color: var(--orange); line-height: 1.1; margin-bottom: 8px;">
            JobPilot
          </h1>
          <h2 style="font-size: 1.4rem; font-weight: 800; color: var(--slate-900); margin-bottom: 14px;">
            AI Career Navigation, ATS Resume Optimization & Live Mock Interview Copilot
          </h2>
          <p style="font-size: 0.9375rem; color: var(--slate-500); max-width: 820px; line-height: 1.6; margin-bottom: 30px;">
            An end-to-end technical career platform delivering 1:1 cross-platform feature parity across native Android (Kotlin / Jetpack Compose) and Windows Desktop (.exe / Edge WebView2) connected to a single live PostgreSQL database on Render.
          </p>

          <div class="grid-3">
            <div class="card" style="padding: 16px; border-color: var(--orange);">
              <div style="font-weight: 800; font-size: 0.875rem; color: var(--orange);">📱 Android Mobile App</div>
              <div style="font-size: 0.75rem; color: var(--slate-500); margin-top: 4px;">Jetpack Compose • ML Kit Face Detection • CameraX Live Stream • SpeechRecognizer</div>
            </div>
            <div class="card" style="padding: 16px; border-color: var(--blue);">
              <div style="font-weight: 800; font-size: 0.875rem; color: var(--blue);">💻 Windows Desktop App</div>
              <div style="font-size: 0.75rem; color: var(--slate-500); margin-top: 4px;">Edge Chromium WebView2 Host • PyInstaller .exe • Inno Setup 6 • Canvas HUD</div>
            </div>
            <div class="card" style="padding: 16px; border-color: var(--purple);">
              <div style="font-weight: 800; font-size: 0.875rem; color: var(--purple);">⚡ Live Cloud Backend</div>
              <div style="font-size: 0.75rem; color: var(--slate-500); margin-top: 4px;">FastAPI AsyncIO • PostgreSQL on Render • Brevo SMTP OTP Two-Factor Auth</div>
            </div>
          </div>
        </div>
      </div>

      <!-- ====================================================================
           SLIDE 2: OBJECTIVE & PROBLEM STATEMENT
           ==================================================================== -->
      <div class="slide" id="slide-2">
        <div class="slide-header">
          <div class="slide-accent-bar"></div>
          <div class="slide-title">1. Project Objective & Problem Statement</div>
          <div class="slide-subtitle">Bridging the gap between candidate qualifications and automated hiring friction</div>
        </div>
        <div class="grid-2">
          <div class="card" style="background: #FEF2F2; border-color: #FCA5A5;">
            <div class="card-title" style="color: #DC2626;">⚠️ Modern Candidate Challenges</div>
            <div class="point-item"><strong>• Black-Box ATS Discard:</strong> Over 75% of qualified resumes are rejected by applicant tracking systems due to parsing incompatibilities and semantic keyword gaps.</div>
            <div class="point-item"><strong>• Lack of Realistic Interview Practice:</strong> No accessible simulators offering live eye-contact tracking, voice dialogue, and STAR-format grading.</div>
            <div class="point-item"><strong>• Disjointed Learning Paths:</strong> Generic video courses lack day-by-day learning accountability, milestone quizzes, and direct job synchronization.</div>
            <div class="point-item"><strong>• Cold Outreach Inefficiency:</strong> High friction in drafting personalized, high-converting recruiter notes and tailored cover letters.</div>
          </div>

          <div class="card card-orange">
            <div class="card-title" style="color: var(--orange);">🚀 The JobPilot AI Solution</div>
            <div class="point-item"><strong>✓ Live AI Mock Interview Room:</strong> 5-stage interactive video simulator with real-time face alignment, voice dictation, and 4-D scorecard.</div>
            <div class="point-item"><strong>✓ Instant ATS Resume Tailoring:</strong> NLP parser extracts candidate skills (PDF/DOCX), audits missing fields, and tailors resumes in 1 click.</div>
            <div class="point-item"><strong>✓ Dynamic 6-Month Roadmaps:</strong> Day-by-day structured curriculum with interactive daily quizzes, code notes, and skill sync.</div>
            <div class="point-item"><strong>✓ Semantic Job Match & Outreach:</strong> Transformer vector similarity scoring with automatic personalized cold outreach message generation.</div>
          </div>
        </div>
      </div>

      <!-- ====================================================================
           SLIDE 3: SYSTEM ARCHITECTURE
           ==================================================================== -->
      <div class="slide" id="slide-3">
        <div class="slide-header">
          <div class="slide-accent-bar"></div>
          <div class="slide-title">2. Unified System Architecture & Tech Stack</div>
          <div class="slide-subtitle">Single source of truth architecture powering complete 1:1 cross-platform parity</div>
        </div>
        <div class="grid-3">
          <div class="card" style="border-color: var(--orange);">
            <div class="card-title" style="color: var(--orange);">📱 Android Mobile</div>
            <div class="point-item"><strong>UI:</strong> Kotlin 2.0 • Jetpack Compose • Material 3 Design</div>
            <div class="point-item"><strong>Vision:</strong> Google ML Kit Face Detection • CameraX Live Stream</div>
            <div class="point-item"><strong>Voice:</strong> Android SpeechRecognizer • TextToSpeech (TTS)</div>
            <div class="point-item"><strong>Network:</strong> Retrofit 2 • OkHttp • Kotlin Coroutines & StateFlow</div>
            <div class="point-item"><strong>Target:</strong> Android APK release (`com.jobpilot.app`)</div>
          </div>

          <div class="card" style="border-color: var(--blue);">
            <div class="card-title" style="color: var(--blue);">💻 Windows Desktop</div>
            <div class="point-item"><strong>Window:</strong> Microsoft Edge Chromium WebView2 Host</div>
            <div class="point-item"><strong>Server:</strong> Python 3.13 Local Reverse Proxy (`desktop/main.py`)</div>
            <div class="point-item"><strong>UI:</strong> React 19 • Vite • Lucide Icons • Canvas Telemetry</div>
            <div class="point-item"><strong>Voice:</strong> Web Speech API (SpeechRecognition) • SpeechSynthesis</div>
            <div class="point-item"><strong>Packaging:</strong> PyInstaller Standalone .exe + Inno Setup 6</div>
          </div>

          <div class="card" style="border-color: var(--purple);">
            <div class="card-title" style="color: var(--purple);">⚡ Production Backend</div>
            <div class="point-item"><strong>Framework:</strong> FastAPI (AsyncIO Python 3.13) High-Throughput REST</div>
            <div class="point-item"><strong>Database:</strong> PostgreSQL hosted on Render Cloud</div>
            <div class="point-item"><strong>ORM:</strong> SQLAlchemy 2.0 • Pydantic V2 Models</div>
            <div class="point-item"><strong>Auth:</strong> JWT Bearer Tokens • Passlib Bcrypt Hashing</div>
            <div class="point-item"><strong>Email:</strong> Brevo SMTP Transactional 6-digit OTP API</div>
          </div>
        </div>
      </div>

      <!-- ====================================================================
           SLIDE 4: CORE FEATURES MATRIX
           ==================================================================== -->
      <div class="slide" id="slide-4">
        <div class="slide-header">
          <div class="slide-accent-bar"></div>
          <div class="slide-title">3. Core Features & Capabilities Matrix</div>
          <div class="slide-subtitle">Six integrated pillars powering candidate preparation, tracking, and evaluation</div>
        </div>
        <div class="grid-2">
          <div class="card">
            <div style="font-weight: 800; font-size: 0.9375rem; color: var(--orange); margin-bottom: 4px;">🎙️ AI Mock Interview Simulator</div>
            <div style="font-size: 0.8125rem; color: var(--slate-700);">5-stage structured technical interview room with on-device camera face presence detection, audio question playback, speech dictation, and 4-D scorecard report.</div>
          </div>
          <div class="card">
            <div style="font-weight: 800; font-size: 0.9375rem; color: var(--orange); margin-bottom: 4px;">🗺️ 6-Month Dynamic Roadmaps</div>
            <div style="font-size: 0.8125rem; color: var(--slate-700);">Customized day-by-day learning milestones with interactive daily quizzes, bookmarking, code notes, and automatic resume skill synchronization.</div>
          </div>
          <div class="card">
            <div style="font-weight: 800; font-size: 0.9375rem; color: var(--orange); margin-bottom: 4px;">📄 ATS Resume Builder & Tailor</div>
            <div style="font-size: 0.8125rem; color: var(--slate-700);">NLP extraction of candidate resumes (PDF/DOCX), automated missing fields audit, ATS score calculation, and 1-click semantic tailoring to job descriptions.</div>
          </div>
          <div class="card">
            <div style="font-weight: 800; font-size: 0.9375rem; color: var(--orange); margin-bottom: 4px;">💼 Semantic Job Match & Outreach</div>
            <div style="font-size: 0.8125rem; color: var(--slate-700);">Calculates exact semantic compatibility %, analyzes skill gaps, and generates tailored LinkedIn outreach messages & cold emails.</div>
          </div>
          <div class="card">
            <div style="font-weight: 800; font-size: 0.9375rem; color: var(--orange); margin-bottom: 4px;">📊 Application Kanban Tracker</div>
            <div style="font-size: 0.8125rem; color: var(--slate-700);">Full lifecycle pipeline tracker organizing applications into Saved, Applied, Interviewing, Offered, and Rejected stages with date tracking.</div>
          </div>
          <div class="card">
            <div style="font-weight: 800; font-size: 0.9375rem; color: var(--orange); margin-bottom: 4px;">🤖 24/7 AI Career Coach</div>
            <div style="font-size: 0.8125rem; color: var(--slate-700);">Conversational AI assistant with full persistent chat history, answering questions regarding career transitions, salary negotiation, and system design.</div>
          </div>
        </div>
      </div>

      <!-- ====================================================================
           SLIDE 5: NLP & AI MODELS (EXACT REQUIREMENT)
           ==================================================================== -->
      <div class="slide" id="slide-5">
        <div class="slide-header">
          <div class="slide-accent-bar"></div>
          <div class="slide-title">4. NLP & AI Models: Where & How Used</div>
          <div class="slide-subtitle">Deep learning, document parsing, embeddings, and computer vision applied across JobPilot</div>
        </div>
        <div style="display: flex; flex-direction: column; gap: 8px;">
          <div class="nlp-box" style="border-left: 4px solid var(--orange);">
            <div class="nlp-box-title">
              <span>1. Transformer Vector Embeddings (BERT / RoBERTa)</span>
              <span style="color: var(--orange);">Semantic Job & Resume Matching</span>
            </div>
            <div class="nlp-box-desc">Transforms job descriptions and candidate skills into dense high-dimensional vectors. Computes cosine similarity to calculate fit percentage (e.g. 94% Match) and identify missing prerequisites.</div>
          </div>

          <div class="nlp-box" style="border-left: 4px solid var(--orange);">
            <div class="nlp-box-title">
              <span>2. Document Information Extraction (Spacy NLP & PDF Engine)</span>
              <span style="color: var(--orange);">Resume Parsing & ATS Scoring</span>
            </div>
            <div class="nlp-box-desc">Extracts structured entities (education, work experience, projects, skills, certifications) from raw PDF and DOCX files. Analyzes keyword density and ATS formatting compliance.</div>
          </div>

          <div class="nlp-box" style="border-left: 4px solid var(--purple);">
            <div class="nlp-box-title">
              <span>3. Generative Language Models (LLM Inference)</span>
              <span style="color: var(--purple);">Career Coach, Roadmap Synthesis & Evaluation</span>
            </div>
            <div class="nlp-box-desc">Synthesizes day-by-day learning milestones, answers coaching queries, generates contextual interview questions tailored to user projects, and produces ideal model answers in evaluation reports.</div>
          </div>

          <div class="nlp-box" style="border-left: 4px solid var(--blue);">
            <div class="nlp-box-title">
              <span>4. Computer Vision (Google ML Kit Face Detection & Canvas HUD)</span>
              <span style="color: var(--blue);">Live Interview Eye Contact & Posture Tracking</span>
            </div>
            <div class="nlp-box-desc">Real-time facial landmark detection running at 30 FPS. Analyzes gaze orientation, face centering, and posture attentiveness to compute an objective Video Presence Score (0-100%).</div>
          </div>

          <div class="nlp-box" style="border-left: 4px solid var(--green);">
            <div class="nlp-box-title">
              <span>5. Speech-to-Text & Text-to-Speech (ASR & TTS Engine)</span>
              <span style="color: var(--green);">Voice-Driven Hands-Free Interview Dialogue</span>
            </div>
            <div class="nlp-box-desc">Web Speech API and Android SpeechRecognizer convert spoken candidate audio into text transcriptions in real time. SpeechSynthesis generates natural spoken audio for the AI interviewer.</div>
          </div>
        </div>
      </div>

      <!-- ====================================================================
           SLIDE 6: ANDROID APK SCREENSHOTS
           ==================================================================== -->
      <div class="slide" id="slide-6">
        <div class="slide-header">
          <div class="slide-accent-bar"></div>
          <div class="slide-title">5. Android APK Application Showcase (Mobile Screens)</div>
          <div class="slide-subtitle">Native Kotlin / Jetpack Compose application with live ML Kit camera and 5-tab navigation</div>
        </div>
        <div class="grid-4" style="align-items: center; justify-items: center;">
          <div style="width: 100%; text-align: center;">
            <img src="{img_mob1}" class="img-preview" alt="Mobile Dashboard">
            <div class="caption">1. Dashboard & Learning Streak</div>
          </div>
          <div style="width: 100%; text-align: center;">
            <img src="{img_mob2}" class="img-preview" alt="AI Mock Interview">
            <div class="caption">2. AI Mock Interview Setup</div>
          </div>
          <div style="width: 100%; text-align: center;">
            <img src="{img_mob3}" class="img-preview" alt="Roadmap Hub">
            <div class="caption">3. 6-Month Career Roadmap</div>
          </div>
          <div style="width: 100%; text-align: center;">
            <img src="{img_mob4}" class="img-preview" alt="ATS Resume Builder">
            <div class="caption">4. ATS Resume Hub & Tailor</div>
          </div>
        </div>
      </div>

      <!-- ====================================================================
           SLIDE 7: WINDOWS DESKTOP SCREENSHOTS
           ==================================================================== -->
      <div class="slide" id="slide-7">
        <div class="slide-header">
          <div class="slide-accent-bar"></div>
          <div class="slide-title">6. Windows Desktop Application Showcase (Desktop Screens)</div>
          <div class="slide-subtitle">Edge Chromium WebView2 Host with 1:1 Jetpack Compose Material 3 styling and embedded proxy</div>
        </div>
        <div class="grid-2" style="align-items: center; justify-items: center;">
          <div style="width: 100%; text-align: center;">
            <img src="{img_desk1}" class="img-preview" alt="Desktop Dashboard">
            <div class="caption">1. Windows Desktop Dashboard & Top Navigation Bar</div>
          </div>
          <div style="width: 100%; text-align: center;">
            <img src="{img_desk2}" class="img-preview" alt="Desktop Authentication">
            <div class="caption">2. Desktop Authentication & Email OTP Verification</div>
          </div>
        </div>
      </div>

      <!-- ====================================================================
           SLIDE 8: DATABASE & PERSISTENCE
           ==================================================================== -->
      <div class="slide" id="slide-8">
        <div class="slide-header">
          <div class="slide-accent-bar"></div>
          <div class="slide-title">7. Database Schema & Backend Data Persistence</div>
          <div class="slide-subtitle">Relational PostgreSQL database schema serving both Android and Windows with zero mock data</div>
        </div>
        <div class="grid-2">
          <div class="card" style="border-left: 4px solid var(--purple);">
            <div style="font-weight: 800; font-size: 0.9375rem; color: var(--purple); margin-bottom: 4px;">👤 Users, Authentication & Profiles</div>
            <div style="font-size: 0.8125rem; color: var(--slate-700);">Stores bcrypt hashed credentials, Brevo 6-digit OTP codes, verification status, candidate target roles, profile strength score (0-100%), and daily learning streak counters.</div>
          </div>
          <div class="card" style="border-left: 4px solid var(--purple);">
            <div style="font-weight: 800; font-size: 0.9375rem; color: var(--purple); margin-bottom: 4px;">🗺️ Roadmaps, Phases & Daily Milestones</div>
            <div style="font-size: 0.8125rem; color: var(--slate-700);">Stores generated curricula, total days (e.g. 180 days), current active day index, progress percentage, completion timestamps, multilingual video links, and code notes.</div>
          </div>
          <div class="card" style="border-left: 4px solid var(--purple);">
            <div style="font-weight: 800; font-size: 0.9375rem; color: var(--purple); margin-bottom: 4px;">📄 Resumes, Missing Fields & ATS Audits</div>
            <div style="font-size: 0.8125rem; color: var(--slate-700);">Stores extracted education, skills, projects, work experience, certifications, missing field diagnostics, and generated tailored resume variations.</div>
          </div>
          <div class="card" style="border-left: 4px solid var(--purple);">
            <div style="font-weight: 800; font-size: 0.9375rem; color: var(--purple); margin-bottom: 4px;">🎙️ Mock Interview Sessions & Reports</div>
            <div style="font-size: 0.8125rem; color: var(--slate-700);">Stores question sets, candidate answer transcripts, camera eye-contact and presence telemetry, and 4-D evaluator reports with ideal model answers.</div>
          </div>
        </div>
      </div>

      <!-- ====================================================================
           SLIDE 9: CONCLUSION & FUTURE SCOPE
           ==================================================================== -->
      <div class="slide" id="slide-9">
        <div class="slide-header">
          <div class="slide-accent-bar"></div>
          <div class="slide-title">8. Project Summary & Future Scope</div>
          <div class="slide-subtitle">Achieving end-to-end multi-platform career acceleration with continuous enhancements</div>
        </div>
        <div class="grid-2">
          <div class="card card-orange">
            <div class="card-title" style="color: var(--orange);">🏆 Key Accomplishments Delivered</div>
            <div class="point-item"><strong>✓ 1:1 Cross-Platform Parity:</strong> Complete visual, functional, and database parity across Android APK and standalone Windows Desktop (.exe).</div>
            <div class="point-item"><strong>✓ Live AI Mock Interview Room:</strong> Video simulator with real-time face tracking, audio synthesis, speech dictation, and comprehensive model answers.</div>
            <div class="point-item"><strong>✓ Automated ATS Pipeline:</strong> Instant resume extraction, missing field audit, and 1-click tailoring with PDF export.</div>
            <div class="point-item"><strong>✓ Production Backend & Auth:</strong> Live PostgreSQL on Render Cloud with Brevo OTP transactional email verification.</div>
          </div>

          <div class="card card-blue">
            <div class="card-title" style="color: var(--blue);">🔮 Future Enhancements & Roadmap</div>
            <div class="point-item"><strong>✦ Autonomous Job Application Bot:</strong> Intelligent autofill and portfolio submission across major hiring boards with user review gates.</div>
            <div class="point-item"><strong>✦ Multilingual Voice & Accent Adaptation:</strong> Expanded speech synthesis supporting international tech interview accents and regional languages.</div>
            <div class="point-item"><strong>✦ Live Coding Sandbox Integration:</strong> Embedded code editor with real-time unit test execution during technical interview stages.</div>
            <div class="point-item"><strong>✦ Enterprise Recruiter Portal:</strong> Direct recruiter portal to review verified candidate interview reports and certified skill badges.</div>
          </div>
        </div>
      </div>

    </div>

    <!-- Controls -->
    <div class="controls-bar">
      <button class="nav-btn" id="prevBtn" onclick="prevSlide()">◀</button>
      <span class="slide-counter" id="counter">Slide 1 / 9</span>
      <button class="nav-btn" id="nextBtn" onclick="nextSlide()">▶</button>
      <button class="nav-btn" onclick="toggleFullScreen()" title="Toggle Fullscreen">⛶</button>
    </div>
  </div>

  <script>
    let currentSlide = 1;
    const totalSlides = 9;

    function showSlide(n) {{
      document.querySelectorAll('.slide').forEach((s, idx) => {{
        s.classList.remove('active');
        if (idx === n - 1) {{
          s.classList.add('active');
        }}
      }});
      document.getElementById('counter').innerText = `Slide ${{n}} / ${{totalSlides}}`;
      document.getElementById('prevBtn').disabled = n === 1;
      document.getElementById('nextBtn').disabled = n === totalSlides;
    }}

    function nextSlide() {{
      if (currentSlide < totalSlides) {{
        currentSlide++;
        showSlide(currentSlide);
      }}
    }}

    function prevSlide() {{
      if (currentSlide > 1) {{
        currentSlide--;
        showSlide(currentSlide);
      }}
    }}

    function toggleFullScreen() {{
      if (!document.fullscreenElement) {{
        document.documentElement.requestFullscreen().catch(() => {{}});
      }} else {{
        if (document.exitFullscreen) {{
          document.exitFullscreen();
        }}
      }}
    }}

    document.addEventListener('keydown', (e) => {{
      if (e.key === 'ArrowRight' || e.key === ' ' || e.key === 'PageDown') {{
        nextSlide();
      }} else if (e.key === 'ArrowLeft' || e.key === 'PageUp') {{
        prevSlide();
      }} else if (e.key === 'Home') {{
        currentSlide = 1;
        showSlide(currentSlide);
      }} else if (e.key === 'End') {{
        currentSlide = totalSlides;
        showSlide(currentSlide);
      }} else if (e.key === 'f' || e.key === 'F') {{
        toggleFullScreen();
      }}
    }});

    showSlide(currentSlide);
  </script>
</body>
</html>
"""

output_html = os.path.join(base_dir, "JobPilot_Presentation.html")
with open(output_html, "w", encoding="utf-8") as f:
    f.write(html_content)

print(f"SUCCESS: Interactive HTML Presentation created at: {output_html} ({os.path.getsize(output_html)} bytes)")
