import os
import sys
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN, MSO_ANCHOR
from pptx.enum.shapes import MSO_SHAPE

def create_presentation():
    prs = Presentation()
    # 16:9 Widescreen dimensions
    prs.slide_width = Inches(13.333)
    prs.slide_height = Inches(7.5)
    
    # Brand Colors
    C_ORANGE = RGBColor(255, 106, 0)       # Core Brand Orange (#FF6A00)
    C_DARK_ORANGE = RGBColor(217, 85, 0)   # #D95500
    C_LIGHT_ORANGE = RGBColor(255, 245, 236)# #FFF5EC
    C_SLATE_900 = RGBColor(15, 23, 42)     # #0F172A
    C_SLATE_700 = RGBColor(51, 65, 85)     # #334155
    C_SLATE_500 = RGBColor(100, 116, 139)  # #64748B
    C_SLATE_100 = RGBColor(241, 245, 249)  # #F1F5F9
    C_WHITE = RGBColor(255, 255, 255)
    C_GREEN = RGBColor(16, 185, 129)       # #10B981
    C_BLUE = RGBColor(2, 132, 199)         # #0284C7
    C_PURPLE = RGBColor(139, 92, 246)      # #8B5CF6

    blank_layout = prs.slide_layouts[6]
    
    upload_dir = r"C:\Users\chetan\.gemini\antigravity\brain\fdd3faad-af8c-41b2-a33d-39b822123ff4\.user_uploaded"

    def add_header(slide, title_text, subtitle_text=""):
        # Top Accent Line
        top_line = slide.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(0.8), Inches(0.5), Inches(1.5), Inches(0.06))
        top_line.fill.solid()
        top_line.fill.fore_color.rgb = C_ORANGE
        top_line.line.color.rgb = C_ORANGE

        # Title
        tx_box = slide.shapes.add_textbox(Inches(0.8), Inches(0.65), Inches(11.7), Inches(0.8))
        tf = tx_box.text_frame
        tf.word_wrap = True
        tf.margin_left = tf.margin_top = tf.margin_right = tf.margin_bottom = 0
        p = tf.paragraphs[0]
        p.text = title_text
        p.font.name = "Segoe UI"
        p.font.size = Pt(24)
        p.font.bold = True
        p.font.color.rgb = C_SLATE_900
        
        if subtitle_text:
            p2 = tf.add_paragraph()
            p2.text = subtitle_text
            p2.font.name = "Segoe UI"
            p2.font.size = Pt(13)
            p2.font.color.rgb = C_SLATE_500
            p2.space_before = Pt(4)

    # =========================================================================
    # SLIDE 1: TITLE SLIDE
    # =========================================================================
    s1 = prs.slides.add_slide(blank_layout)
    # Background subtle card
    bg = s1.shapes.add_shape(MSO_SHAPE.RECTANGLE, 0, 0, Inches(13.333), Inches(7.5))
    bg.fill.solid()
    bg.fill.fore_color.rgb = RGBColor(255, 253, 251) # BgWarmWhite
    bg.line.fill.background()

    # Left Gradient Accent Card
    accent_card = s1.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(1.0), Inches(11.733), Inches(5.5))
    accent_card.fill.solid()
    accent_card.fill.fore_color.rgb = C_WHITE
    accent_card.line.color.rgb = RGBColor(255, 212, 168)
    accent_card.line.width = Pt(1.5)

    # Brand Badge
    badge = s1.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(1.3), Inches(1.5), Inches(2.2), Inches(0.45))
    badge.fill.solid()
    badge.fill.fore_color.rgb = C_LIGHT_ORANGE
    badge.line.color.rgb = C_ORANGE
    badge_tf = badge.text_frame
    badge_tf.vertical_anchor = MSO_ANCHOR.MIDDLE
    bp = badge_tf.paragraphs[0]
    bp.alignment = PP_ALIGN.CENTER
    bp.text = "✦ AI CAREER COPILOT"
    bp.font.name = "Segoe UI"
    bp.font.size = Pt(11)
    bp.font.bold = True
    bp.font.color.rgb = C_ORANGE

    # Main Title
    t_box = s1.shapes.add_textbox(Inches(1.3), Inches(2.2), Inches(10.5), Inches(2.0))
    tf = t_box.text_frame
    tf.word_wrap = True
    p1 = tf.paragraphs[0]
    p1.text = "JobPilot"
    p1.font.name = "Segoe UI"
    p1.font.size = Pt(48)
    p1.font.bold = True
    p1.font.color.rgb = C_ORANGE

    p2 = tf.add_paragraph()
    p2.text = "AI-Powered Career Navigation, ATS Optimization & Mock Interview Platform"
    p2.font.name = "Segoe UI"
    p2.font.size = Pt(20)
    p2.font.bold = True
    p2.font.color.rgb = C_SLATE_900
    p2.space_before = Pt(8)

    p3 = tf.add_paragraph()
    p3.text = "Unified 1:1 Architecture: Native Android APK (Kotlin / Compose) & Windows Desktop (Edge WebView2) • Live PostgreSQL Backend"
    p3.font.name = "Segoe UI"
    p3.font.size = Pt(13)
    p3.font.color.rgb = C_SLATE_500
    p3.space_before = Pt(8)

    # 3 Key Pills at bottom of title
    pills_data = [
        ("📱 Android Mobile App", "Jetpack Compose + ML Kit Face Detection"),
        ("💻 Windows Desktop App", "Standalone .exe + Edge WebView2 Host"),
        ("⚡ Live Production Backend", "FastAPI + PostgreSQL + Brevo OTP on Render")
    ]
    for idx, (head, sub) in enumerate(pills_data):
        px = Inches(1.3 + idx * 3.6)
        pill = s1.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, px, Inches(4.7), Inches(3.4), Inches(1.2))
        pill.fill.solid()
        pill.fill.fore_color.rgb = C_SLATE_100
        pill.line.color.rgb = RGBColor(226, 232, 240)
        ptf = pill.text_frame
        ptf.margin_left = ptf.margin_right = Inches(0.2)
        ptf.margin_top = Inches(0.15)
        pp1 = ptf.paragraphs[0]
        pp1.text = head
        pp1.font.name = "Segoe UI"
        pp1.font.size = Pt(13)
        pp1.font.bold = True
        pp1.font.color.rgb = C_SLATE_900

        pp2 = ptf.add_paragraph()
        pp2.text = sub
        pp2.font.name = "Segoe UI"
        pp2.font.size = Pt(10)
        pp2.font.color.rgb = C_SLATE_700
        pp2.space_before = Pt(4)

    # =========================================================================
    # SLIDE 2: PROJECT OBJECTIVES & PROBLEM STATEMENT
    # =========================================================================
    s2 = prs.slides.add_slide(blank_layout)
    add_header(s2, "1. Project Objective & Problem Statement", "Solving modern technical hiring friction through automation, real-time feedback, and dynamic curriculum")

    # Left Card: Problem Statement
    c_prob = s2.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(1.7), Inches(5.6), Inches(5.2))
    c_prob.fill.solid()
    c_prob.fill.fore_color.rgb = RGBColor(254, 242, 242) # Red-50
    c_prob.line.color.rgb = RGBColor(252, 165, 165)
    c_ptf = c_prob.text_frame
    c_ptf.margin_left = c_ptf.margin_right = Inches(0.3)
    c_ptf.margin_top = Inches(0.3)
    
    cp_h = c_ptf.paragraphs[0]
    cp_h.text = "⚠️ The Modern Candidate Challenges"
    cp_h.font.name = "Segoe UI"
    cp_h.font.size = Pt(16)
    cp_h.font.bold = True
    cp_h.font.color.rgb = RGBColor(185, 28, 28)

    prob_points = [
        ("Black-Box ATS Screening", "Over 75% of resumes are discarded by Applicant Tracking Systems due to formatting errors and semantic skill mismatches."),
        ("No Realistic Interview Simulator", "Candidates lack structured practice with real-time video eye-contact telemetry, voice dialogue, and STAR methodology grading."),
        ("Fragmented Learning Paths", "Generic online courses lack day-by-day accountability, quizzes, and automated synchronization with real hiring requirements."),
        ("Cold Outreach Inefficiency", "Job seekers struggle to write personalized, high-converting recruiter messages and cover letters tailored to job descriptions.")
    ]
    for title, desc in prob_points:
        p_t = c_ptf.add_paragraph()
        p_t.text = f"• {title}: "
        p_t.font.name = "Segoe UI"
        p_t.font.size = Pt(11)
        p_t.font.bold = True
        p_t.font.color.rgb = C_SLATE_900
        p_t.space_before = Pt(10)
        
        # Add body in same run
        run = p_t.add_run()
        run.text = desc
        run.font.bold = False
        run.font.color.rgb = C_SLATE_700

    # Right Card: The JobPilot Solution
    c_sol = s2.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(6.8), Inches(1.7), Inches(5.7), Inches(5.2))
    c_sol.fill.solid()
    c_sol.fill.fore_color.rgb = C_LIGHT_ORANGE
    c_sol.line.color.rgb = C_ORANGE
    c_stf = c_sol.text_frame
    c_stf.margin_left = c_stf.margin_right = Inches(0.3)
    c_stf.margin_top = Inches(0.3)

    cs_h = c_stf.paragraphs[0]
    cs_h.text = "🚀 The JobPilot AI Solution"
    cs_h.font.name = "Segoe UI"
    cs_h.font.size = Pt(16)
    cs_h.font.bold = True
    cs_h.font.color.rgb = C_DARK_ORANGE

    sol_points = [
        ("AI Mock Interview Simulator", "5-stage structured technical interview room with on-device face presence tracking, audio synthesis, voice dictation, and 4-D report."),
        ("Automated ATS Resume Tailor", "NLP extraction of candidate resumes (PDF/DOCX), audit of missing fields, and instant 1-click tailoring against any job title."),
        ("Personalized 6-Month Roadmaps", "Custom curriculum generated for any tech role with day-by-day learning topics, daily quizzes, and code notes."),
        ("Semantic Vector Job Match & Gap Analysis", "Calculates exact match percentage, missing required skills, and generates customized cold outreach messages.")
    ]
    for title, desc in sol_points:
        p_t = c_stf.add_paragraph()
        p_t.text = f"✓ {title}: "
        p_t.font.name = "Segoe UI"
        p_t.font.size = Pt(11)
        p_t.font.bold = True
        p_t.font.color.rgb = C_DARK_ORANGE
        p_t.space_before = Pt(10)
        
        run = p_t.add_run()
        run.text = desc
        run.font.bold = False
        run.font.color.rgb = C_SLATE_900

    # =========================================================================
    # SLIDE 3: SYSTEM ARCHITECTURE & TECH STACK
    # =========================================================================
    s3 = prs.slides.add_slide(blank_layout)
    add_header(s3, "2. System Architecture & Cross-Platform Tech Stack", "Single production backend powering 1:1 parity between Android APK and Windows Desktop")

    arch_cards = [
        ("📱 Android Mobile Client", [
            ("Language / UI", "Kotlin 2.0 • Jetpack Compose • Material 3 Design"),
            ("ML & Vision", "Google ML Kit Face Detection • CameraX Live Stream"),
            ("Voice & Audio", "Android SpeechRecognizer • TextToSpeech (TTS) Engine"),
            ("Networking & State", "Retrofit 2 • OkHttp • Kotlin Coroutines & StateFlow"),
            ("Packaging", "Android APK (`com.jobpilot.app`) Release Build")
        ], C_ORANGE),
        ("💻 Windows Desktop Client", [
            ("Runtime / Window", "Microsoft Edge Chromium WebView2 Native Window"),
            ("Embedded Host", "Python 3.13 Local SPA & Reverse Proxy Server (`main.py`)"),
            ("Frontend UI", "React 19 • Vite • Lucide Icons • Canvas Vision Telemetry"),
            ("Audio & Voice", "Web Speech API (SpeechRecognition) • SpeechSynthesis"),
            ("Distribution", "PyInstaller Standalone Executable + Inno Setup 6 Installer")
        ], C_BLUE),
        ("⚡ Production Cloud Backend", [
            ("Framework", "FastAPI (AsyncIO Python 3.13) • High-throughput REST API"),
            ("Database", "PostgreSQL running on Render Cloud (Single Source of Truth)"),
            ("ORM & Schemas", "SQLAlchemy 2.0 • Pydantic V2 Validation Models"),
            ("Security & Auth", "JWT Bearer Tokens • Passlib Bcrypt Hashing"),
            ("Email Delivery", "Brevo SMTP Transactional Email API for 6-digit OTP verification")
        ], C_PURPLE)
    ]

    for idx, (title, items, col) in enumerate(arch_cards):
        ax = Inches(0.8 + idx * 4.0)
        ac = s3.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, ax, Inches(1.7), Inches(3.8), Inches(5.2))
        ac.fill.solid()
        ac.fill.fore_color.rgb = C_WHITE
        ac.line.color.rgb = col
        ac.line.width = Pt(2)
        
        atf = ac.text_frame
        atf.margin_left = atf.margin_right = Inches(0.25)
        atf.margin_top = Inches(0.25)
        
        ah = atf.paragraphs[0]
        ah.text = title
        ah.font.name = "Segoe UI"
        ah.font.size = Pt(14)
        ah.font.bold = True
        ah.font.color.rgb = col

        for k, v in items:
            p = atf.add_paragraph()
            p.text = f"{k}: "
            p.font.name = "Segoe UI"
            p.font.size = Pt(10.5)
            p.font.bold = True
            p.font.color.rgb = C_SLATE_900
            p.space_before = Pt(8)

            run = p.add_run()
            run.text = v
            run.font.bold = False
            run.font.color.rgb = C_SLATE_700

    # =========================================================================
    # SLIDE 4: CORE PLATFORM FEATURES MATRIX
    # =========================================================================
    s4 = prs.slides.add_slide(blank_layout)
    add_header(s4, "3. Core Features & Capabilities Matrix", "Comprehensive breakdown of the 6 integrated career acceleration modules")

    features = [
        ("🎙️ AI Mock Interview Simulator", "5-stage structured interview room with on-device camera face presence detection, question audio playback, voice dictation, and instant 4-D scorecard report."),
        ("🗺️ 6-Month Dynamic Roadmaps", "Customized day-by-day learning milestones with interactive daily quizzes, bookmarking, code notes, and automatic resume skill synchronization."),
        ("📄 ATS Resume Builder & Tailor", "NLP extraction of candidate resumes (PDF/DOCX), automated missing fields audit, ATS score calculation, and 1-click semantic tailoring to job descriptions."),
        ("💼 Semantic Job Match & Outreach", "Calculates exact semantic compatibility %, analyzes skill gaps, and generates tailored LinkedIn outreach messages & cold emails."),
        ("📊 Application Kanban Tracker", "Full lifecycle pipeline tracker organizing applications into Saved, Applied, Interviewing, Offered, and Rejected stages with date tracking."),
        ("🤖 24/7 AI Career Coach", "Conversational AI assistant with full persistent chat history, answering questions regarding career transitions, salary negotiation, and system design.")
    ]

    for idx, (head, desc) in enumerate(features):
        row = idx // 2
        col = idx % 2
        fx = Inches(0.8 + col * 5.95)
        fy = Inches(1.7 + row * 1.75)
        
        fc = s4.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, fx, fy, Inches(5.75), Inches(1.55))
        fc.fill.solid()
        fc.fill.fore_color.rgb = C_WHITE
        fc.line.color.rgb = RGBColor(226, 232, 240)
        
        ftf = fc.text_frame
        ftf.margin_left = ftf.margin_right = Inches(0.25)
        ftf.margin_top = Inches(0.2)
        
        fh = ftf.paragraphs[0]
        fh.text = head
        fh.font.name = "Segoe UI"
        fh.font.size = Pt(13)
        fh.font.bold = True
        fh.font.color.rgb = C_ORANGE

        fd = ftf.add_paragraph()
        fd.text = desc
        fd.font.name = "Segoe UI"
        fd.font.size = Pt(10)
        fd.font.color.rgb = C_SLATE_700
        fd.space_before = Pt(4)

    # =========================================================================
    # SLIDE 5: NLP & ARTIFICIAL INTELLIGENCE MODELS (EXACT PROMPT REQUIREMENT)
    # =========================================================================
    s5 = prs.slides.add_slide(blank_layout)
    add_header(s5, "4. NLP & Artificial Intelligence Models: Where & How Used", "Detailed mapping of Machine Learning, Natural Language Processing, and Computer Vision models in JobPilot")

    nlp_models = [
        ("1. Transformer Vector Embeddings (BERT / RoBERTa)", 
         "Semantic Job Matching & Resume Relevance",
         "Generates high-dimensional dense vector embeddings of job descriptions and candidate profile skills. Computes cosine similarity to generate accurate Match Percentage and identify missing required technical skills."),
        ("2. Document Information Extraction (Spacy NLP & PDF Engine)",
         "Resume Parsing & ATS Scoring Pipeline",
         "Extracts entities (Work Experience, Education, Skills, Certifications) from uploaded PDF and DOCX files. Performs keyword density analysis and structural formatting checks to calculate ATS compatibility score."),
        ("3. Generative Language Model (LLM Inference)",
         "Career Coach, Roadmap Synthesis & Interview Feedback",
         "Generates customized 6-month day-by-day learning curricula, multi-turn conversational coaching responses, dynamic interview questions tailored to candidates' projects, and detailed evaluator feedback with ideal model answers."),
        ("4. Computer Vision Face Detection (Google ML Kit & Canvas Telemetry)",
         "Live Interview Face Alignment & Presence Score",
         "Executes real-time face detection at 30 FPS. Analyzes face orientation, bounding box centering, eye-contact estimation, and posture attentiveness to produce an objective Video Presence Score (0-100%)."),
        ("5. Voice Transcription & Speech Synthesis (ASR & TTS Engine)",
         "Voice-Driven Hands-Free Mock Interview",
         "Web Speech API and Android SpeechRecognizer convert spoken candidate audio into text transcriptions in real time. SpeechSynthesis generates natural spoken audio for the AI interviewer.")
    ]

    for idx, (m_title, m_use, m_desc) in enumerate(nlp_models):
        my = Inches(1.65 + idx * 1.05)
        mc = s5.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), my, Inches(11.733), Inches(0.95))
        mc.fill.solid()
        mc.fill.fore_color.rgb = C_WHITE
        mc.line.color.rgb = C_ORANGE if idx in (0, 1, 2) else C_BLUE
        mc.line.width = Pt(1.5)
        
        mtf = mc.text_frame
        mtf.margin_left = mtf.margin_right = Inches(0.25)
        mtf.margin_top = Inches(0.12)
        
        mh = mtf.paragraphs[0]
        mh.text = f"{m_title}  •  "
        mh.font.name = "Segoe UI"
        mh.font.size = Pt(11.5)
        mh.font.bold = True
        mh.font.color.rgb = C_SLATE_900

        r_use = mh.add_run()
        r_use.text = f"Purpose: {m_use}"
        r_use.font.bold = True
        r_use.font.color.rgb = C_ORANGE if idx in (0, 1, 2) else C_BLUE

        md = mtf.add_paragraph()
        md.text = m_desc
        md.font.name = "Segoe UI"
        md.font.size = Pt(9.5)
        md.font.color.rgb = C_SLATE_700
        md.space_before = Pt(2)

    # =========================================================================
    # SLIDE 6: ANDROID APK APPLICATION SCREENSHOTS
    # =========================================================================
    s6 = prs.slides.add_slide(blank_layout)
    add_header(s6, "5. Android APK Application Showcase (Mobile Screens)", "Jetpack Compose Material 3 UI with Live ML Kit Camera, Roadmap Quizzes, and ATS Resume Hub")

    # Let's place 4 Android portrait screenshots
    android_imgs = [
        ("media_1789363134256.jpg", "Mobile Dashboard & Metrics"),
        ("media_1789363134449.jpg", "AI Mock Interview Setup"),
        ("media_1789380349316.jpg", "6-Month Career Roadmap"),
        ("media_1789403199429.png", "ATS Resume Hub & Tailor")
    ]

    for idx, (img_file, caption) in enumerate(android_imgs):
        ix = Inches(0.8 + idx * 2.95)
        img_path = os.path.join(upload_dir, img_file)
        if os.path.exists(img_path):
            s6.shapes.add_picture(img_path, ix, Inches(1.7), width=Inches(2.75))
            
            # Caption Box below
            cap = s6.shapes.add_textbox(ix, Inches(6.75), Inches(2.75), Inches(0.4))
            ctf = cap.text_frame
            ctf.margin_top = ctf.margin_left = ctf.margin_right = ctf.margin_bottom = 0
            cp = ctf.paragraphs[0]
            cp.text = caption
            cp.alignment = PP_ALIGN.CENTER
            cp.font.name = "Segoe UI"
            cp.font.size = Pt(10)
            cp.font.bold = True
            cp.font.color.rgb = C_SLATE_900

    # =========================================================================
    # SLIDE 7: WINDOWS DESKTOP APPLICATION SCREENSHOTS
    # =========================================================================
    s7 = prs.slides.add_slide(blank_layout)
    add_header(s7, "6. Windows Desktop Application Showcase (Desktop Screens)", "Edge WebView2 Native Window Host with 1:1 Material 3 Navigation & Embedded Reverse Proxy")

    # Let's place 2 large Windows Desktop screenshots
    desktop_imgs = [
        ("media_1789892967611.png", "Windows Desktop Dashboard & Top Navigation Bar"),
        ("media_1789916458446.png", "Windows Authentication & OTP Verification Screen")
    ]

    for idx, (img_file, caption) in enumerate(desktop_imgs):
        ix = Inches(0.8 + idx * 5.95)
        img_path = os.path.join(upload_dir, img_file)
        if os.path.exists(img_path):
            s7.shapes.add_picture(img_path, ix, Inches(1.75), width=Inches(5.75))
            
            cap = s7.shapes.add_textbox(ix, Inches(6.4), Inches(5.75), Inches(0.5))
            ctf = cap.text_frame
            ctf.margin_top = ctf.margin_left = ctf.margin_right = ctf.margin_bottom = 0
            cp = ctf.paragraphs[0]
            cp.text = caption
            cp.alignment = PP_ALIGN.CENTER
            cp.font.name = "Segoe UI"
            cp.font.size = Pt(11)
            cp.font.bold = True
            cp.font.color.rgb = C_SLATE_900

    # =========================================================================
    # SLIDE 8: DATABASE & UNIFIED BACKEND PARITY
    # =========================================================================
    s8 = prs.slides.add_slide(blank_layout)
    add_header(s8, "7. Database Schema & Backend Data Persistence", "Single source of truth on PostgreSQL with zero mock bypasses across Android and Windows")

    db_tables = [
        ("👤 Users & Auth", "Stores encrypted credentials (bcrypt), Brevo email OTP timestamps, verification states, streak counters, and target roles."),
        ("🗺️ Roadmaps & Milestones", "Stores generated curricula, total days (e.g. 180), active day index, progress %, completion timestamps, and phase resources."),
        ("📝 Daily Quizzes & Notes", "Stores daily 3-question quizzes, selected candidate answers, pass/fail status, and user markdown code notes per day."),
        ("📄 Resumes & ATS Audits", "Stores extracted personal info, education, skills, projects, work experience, missing field audits, and tailored resume variations."),
        ("💼 Jobs & Applications", "Stores job postings, required skills, semantic vector match scores, and application status stages (Saved $\\rightarrow$ Offered)."),
        ("🎙️ Interview Sessions & Reports", "Stores question sets, candidate answer transcripts, camera eye contact & presence telemetry, and 4-D evaluator reports.")
    ]

    for idx, (t_name, t_desc) in enumerate(db_tables):
        row = idx // 2
        col = idx % 2
        tx = Inches(0.8 + col * 5.95)
        ty = Inches(1.7 + row * 1.75)
        
        tc = s8.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, tx, ty, Inches(5.75), Inches(1.55))
        tc.fill.solid()
        tc.fill.fore_color.rgb = C_WHITE
        tc.line.color.rgb = C_PURPLE
        tc.line.width = Pt(1.5)
        
        ttf = tc.text_frame
        ttf.margin_left = ttf.margin_right = Inches(0.25)
        ttf.margin_top = Inches(0.2)
        
        th = ttf.paragraphs[0]
        th.text = t_name
        th.font.name = "Segoe UI"
        th.font.size = Pt(13)
        th.font.bold = True
        th.font.color.rgb = C_PURPLE

        td = ttf.add_paragraph()
        td.text = t_desc
        td.font.name = "Segoe UI"
        td.font.size = Pt(10)
        td.font.color.rgb = C_SLATE_700
        td.space_before = Pt(4)

    # =========================================================================
    # SLIDE 9: CONCLUSION & FUTURE SCOPE
    # =========================================================================
    s9 = prs.slides.add_slide(blank_layout)
    add_header(s9, "8. Project Summary & Future Enhancements", "Transforming technical recruitment with seamless multi-platform AI copiloting")

    # Accomplishments Box
    c_acc = s9.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(0.8), Inches(1.7), Inches(5.6), Inches(5.2))
    c_acc.fill.solid()
    c_acc.fill.fore_color.rgb = C_LIGHT_ORANGE
    c_acc.line.color.rgb = C_ORANGE
    c_acc.line.width = Pt(1.5)
    
    atf = c_acc.text_frame
    atf.margin_left = atf.margin_right = Inches(0.3)
    atf.margin_top = Inches(0.3)
    
    ah = atf.paragraphs[0]
    ah.text = "🏆 Key Accomplishments Delivered"
    ah.font.name = "Segoe UI"
    ah.font.size = Pt(15)
    ah.font.bold = True
    ah.font.color.rgb = C_DARK_ORANGE

    acc_points = [
        ("1:1 Multi-Platform Parity", "Delivered complete feature, navigation, and visual parity across native Android APK and standalone Windows Desktop .exe."),
        ("Live AI Mock Interview Room", "Interactive video simulator with real-time face tracking, audio synthesis, speech-to-text dictation, and comprehensive model answers."),
        ("End-to-End ATS Pipeline", "Instant extraction, missing field audit, and 1-click tailoring with PDF export."),
        ("Enterprise-Grade Cloud Backend", "FastAPI + PostgreSQL with Brevo OTP transactional email delivery and JWT authentication.")
    ]
    for title, desc in acc_points:
        p = atf.add_paragraph()
        p.text = f"✓ {title}: "
        p.font.name = "Segoe UI"
        p.font.size = Pt(11)
        p.font.bold = True
        p.font.color.rgb = C_DARK_ORANGE
        p.space_before = Pt(10)
        run = p.add_run()
        run.text = desc
        run.font.bold = False
        run.font.color.rgb = C_SLATE_900

    # Future Scope Box
    c_fut = s9.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(6.8), Inches(1.7), Inches(5.7), Inches(5.2))
    c_fut.fill.solid()
    c_fut.fill.fore_color.rgb = RGBColor(240, 249, 255) # Blue-50
    c_fut.line.color.rgb = C_BLUE
    c_fut.line.width = Pt(1.5)
    
    ftf = c_fut.text_frame
    ftf.margin_left = ftf.margin_right = Inches(0.3)
    ftf.margin_top = Inches(0.3)
    
    fh = ftf.paragraphs[0]
    fh.text = "🔮 Future Roadmap & Enhancements"
    fh.font.name = "Segoe UI"
    fh.font.size = Pt(15)
    fh.font.bold = True
    fh.font.color.rgb = C_BLUE

    fut_points = [
        ("Autonomous Job Application Agent", "Automated form filling and portfolio submission across major job portals with user approval gates."),
        ("Multilingual Voice & Accent Adaptation", "Expanded speech synthesis supporting international tech interview accents and regional languages."),
        ("Live Coding Sandbox Integration", "Embedded Monaco / WebAssembly code editor with unit test execution during technical interview stages."),
        ("Enterprise Recruiter Portal", "Dedicated portal for recruiters to review candidates' verified AI interview scorecards and roadmap credentials.")
    ]
    for title, desc in fut_points:
        p = ftf.add_paragraph()
        p.text = f"✦ {title}: "
        p.font.name = "Segoe UI"
        p.font.size = Pt(11)
        p.font.bold = True
        p.font.color.rgb = C_BLUE
        p.space_before = Pt(10)
        run = p.add_run()
        run.text = desc
        run.font.bold = False
        run.font.color.rgb = C_SLATE_900

    # Save PPTX file
    output_path = os.path.join(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")), "JobPilot_Presentation.pptx")
    prs.save(output_path)
    print(f"SUCCESS: Presentation created at: {output_path} ({os.path.getsize(output_path)} bytes)")

if __name__ == "__main__":
    create_presentation()
