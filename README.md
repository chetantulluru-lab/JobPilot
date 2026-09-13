# JobPilot — Official Public Website

> **Your career. Piloted by AI.**

The official public-facing website for **JobPilot**, the AI & NLP-driven career companion for Android.

---

## ✦ Overview

The JobPilot website serves as the primary gateway to introduce candidates, students, and employers to the JobPilot ecosystem. It explains the core Natural Language Processing (NLP) intelligence, demonstrates semantic skill matching, and provides a direct gateway to download the official Android application from the Google Play Store.

> **Architecture Notice**: The website is the official informational and promotion portal. The full career management platform, real-time profile synchronization, and job application workflows are natively hosted within the **JobPilot Android Application** (Android 11+).

---

## 🚀 Key Features Demonstrated

- **AI Resume Intelligence**: NLP-based parsing that transforms unstructured resumes (PDF/DOCX) into normalized career profiles.
- **Smart Job Matching**: 0–100% semantic compatibility scoring with transparent strong matches and skill gap diagnoses.
- **Personalized Career Profile**: Single source of truth for coursework, projects, frameworks, and career aspirations.
- **AI Career Insights**: Actionable guidance highlighting what skills or projects will boost candidate competitiveness.
- **Application Assistance**: Assisted preparation for job applications using structured profile data.
- **Application Tracking**: Integrated mobile pipeline to monitor deadlines, interviews, and recruiter updates.

---

## 🎨 Design System

- **Palette**: White / Warm White (`#FFFFFF`, `#FFFDFB`) + Electric Warm Orange (`#FF6A00`, `#FF5500`)
- **Aesthetic**: Apple-clean whitespace, glassmorphism, soft orange ambient lighting, 1px subtle borders, large rounded radii (20px–32px).
- **Signature AI Core**: Floating glowing AI orb with orbiting particles and live career insight glass cards.
- **Mobile Mockup**: Realistic Android 11+ chassis featuring live career feed, profile strength meter (86%), and job cards.

---

## 📱 Google Play Store Configuration

To update the Google Play Store destination URL across the **entire website**, edit the single centralized value in:

```javascript
// src/config/appConfig.js
export const APP_CONFIG = {
  // ...
  PLAY_STORE_URL: 'https://play.google.com/store/apps/details?id=com.jobpilot.app',
  // ...
};
```

All buttons, badges, and download triggers dynamically reference `APP_CONFIG.PLAY_STORE_URL`.

---

## 🛠️ Tech Stack

- **React 19**
- **Vite 6**
- **React Router 6**
- **Lucide React** (clean, accessible vector icons)
- **Modern Vanilla CSS** (CSS Variables, Glassmorphism, CSS Grid, Flexbox)

---

## 📂 Project Structure

```
JobPilot/
├── public/
│   └── favicon.svg           # Custom JobPilot AI spark favicon
├── src/
│   ├── config/
│   │   └── appConfig.js      # Centralized app configuration & PLAY_STORE_URL
│   ├── data/
│   │   ├── features.js       # Features data & student challenges
│   │   ├── mockJobs.js       # Mock jobs & NLP pipeline definitions
│   │   └── steps.js          # How It Works 01–06 steps
│   ├── styles/
│   │   ├── variables.css     # Brand tokens, colors, glassmorphism, shadows
│   │   ├── base.css          # Resets, typography, layout utilities
│   │   └── components.css    # Navbar, Phone mockup, AI Orb, Job cards, etc.
│   ├── components/
│   │   ├── AIOrb.jsx         # Signature floating AI visual
│   │   ├── Button.jsx        # Reusable button with router/href support
│   │   ├── FeatureCard.jsx   # Feature card with badge & disclaimers
│   │   ├── Footer.jsx        # Standardized footer with legal & product links
│   │   ├── GlassCard.jsx     # Reusable glassmorphic container
│   │   ├── GlowBackground.jsx# Ambient orange glow lighting
│   │   ├── JobMatchCard.jsx  # Interactive match score demonstration
│   │   ├── Navbar.jsx        # Sticky translucent header with mobile drawer
│   │   ├── NLPPipelineVisual.jsx # Interactive 8-stage NLP pipeline inspector
│   │   ├── PhoneMockup.jsx   # Realistic Android phone chassis with UI
│   │   ├── PlayStoreButton.jsx # Google Play button mapped to PLAY_STORE_URL
│   │   ├── QRPlaceholder.jsx # Vector QR placeholder for Android download
│   │   ├── ScrollToTop.jsx   # Auto-scroll on route transition
│   │   ├── SectionHeading.jsx# Standardized section headings
│   │   └── StepCard.jsx      # Step card for timeline
│   ├── pages/
│   │   ├── Home.jsx          # Hero, Trust strip, NLP, Job match, Students, Android promo
│   │   ├── Features.jsx      # Dedicated features breakdown & ethics
│   │   ├── HowItWorks.jsx    # Step-by-step 01–06 interactive workflow
│   │   ├── About.jsx         # Problem statement, mission & principles
│   │   ├── Download.jsx      # Android 11+ download showcase & QR code
│   │   ├── Privacy.jsx       # 12-section project privacy policy
│   │   ├── Terms.jsx         # 14-section terms with employment disclaimers
│   │   └── Contact.jsx       # Categorized contact form & support channels
│   ├── App.jsx               # Routes setup & 404 fallback
│   ├── index.css             # Entry point importing custom styles
│   └── main.jsx              # React DOM root
├── index.html                # SEO meta tags, OpenGraph, custom typography
└── package.json
```

---

## ⚡ Development & Build Commands

```bash
# Install dependencies
npm install

# Start local development server
npm run dev

# Production build
npm run build

# Preview production build locally
npm run preview
```

---

## ⚖️ License & Legal

© 2026 JobPilot. All rights reserved.  
*Disclaimer: JobPilot does not guarantee employment, job interviews, or job offers. All career matching is advisory and powered by natural language processing.*
