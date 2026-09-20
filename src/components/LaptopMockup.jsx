import React from 'react';
import { 
  Sparkles, 
  Video, 
  CheckCircle2, 
  Mic, 
  Eye, 
  ShieldCheck, 
  ChevronRight,
  Zap,
  Volume2
} from 'lucide-react';

/**
 * 3D Metallic Laptop Mockup Component
 * Visualizes the JobPilot Desktop experience running the AI Mock Interview Simulator,
 * Face Detection Presence tracker, and 1-Click ATS Tailor.
 */
export default function LaptopMockup({ className = '' }) {
  return (
    <div className={`laptop-mockup-wrapper ${className}`}>
      {/* 3D Ambient Holographic Backlight */}
      <div className="laptop-ambient-glow" />

      {/* Main 3D Laptop Container with Perspective */}
      <div className="laptop-perspective-container">
        {/* Laptop Display Lid */}
        <div className="laptop-screen-lid">
          {/* Webcam & Sensor Bar */}
          <div className="laptop-webcam-bar">
            <span className="laptop-camera-lens" />
            <span className="laptop-camera-indicator-active" title="ML Kit Face Detection On" />
          </div>

          {/* Screen Bezel & Display */}
          <div className="laptop-screen-display">
            {/* Desktop App Window Header */}
            <div className="laptop-window-header">
              <div className="laptop-window-controls">
                <span className="window-dot dot-red" />
                <span className="window-dot dot-yellow" />
                <span className="window-dot dot-green" />
              </div>
              <div className="laptop-window-title">
                <Sparkles size={12} color="#FF6A00" />
                <span>JobPilot Desktop • AI Mock Interview Simulator (Live Session)</span>
              </div>
              <div className="laptop-status-badge">
                <span className="status-live-pulse" />
                <span>LIVE RECORDING</span>
              </div>
            </div>

            {/* Application Interface Grid */}
            <div className="laptop-app-body">
              {/* Left Column: Interviewer AI Voice & Real-time Question */}
              <div className="laptop-app-left">
                <div className="laptop-session-meta">
                  <span className="badge-chip badge-chip-orange">Question 2 of 5</span>
                  <span className="badge-chip badge-chip-neutral">Backend & Distributed Systems</span>
                </div>

                <div className="laptop-interviewer-card">
                  <div className="interviewer-header">
                    <div className="interviewer-avatar">
                      <Sparkles size={16} color="#FFFFFF" />
                    </div>
                    <div>
                      <div className="interviewer-name">AI Hiring Lead</div>
                      <div className="interviewer-role">Senior Engineering Evaluator</div>
                    </div>
                    <div className="interviewer-audio-wave">
                      <Volume2 size={14} color="#FF6A00" />
                      <span className="audio-bar bar-1" />
                      <span className="audio-bar bar-2" />
                      <span className="audio-bar bar-3" />
                      <span className="audio-bar bar-4" />
                    </div>
                  </div>

                  <p className="interviewer-prompt">
                    "Tell me about a challenging distributed concurrency or database deadlock scenario you solved. How did you structure your lock hierarchy?"
                  </p>
                </div>

                {/* Real-time speech response preview */}
                <div className="laptop-candidate-speech">
                  <div className="speech-label">
                    <Mic size={13} color="#10B981" />
                    <span>Candidate Speech (Voice Dictation Active)</span>
                  </div>
                  <p className="speech-text">
                    "In our FastAPI service, we isolated background task workers using Redis distributed locks with exponential backoff..."
                  </p>
                </div>
              </div>

              {/* Right Column: Live Video Feed with ML Kit Face Detection */}
              <div className="laptop-app-right">
                <div className="laptop-video-feed">
                  {/* Video Background Mock */}
                  <div className="feed-mock-image">
                    <div className="feed-face-box">
                      <div className="face-corner tl" />
                      <div className="face-corner tr" />
                      <div className="face-corner bl" />
                      <div className="face-corner br" />
                      <span className="face-tag">Candidate Centered • 98%</span>
                    </div>
                  </div>

                  {/* On-Screen Telemetry HUD */}
                  <div className="feed-hud-overlay">
                    <div className="hud-metric">
                      <Eye size={12} color="#10B981" />
                      <span>Eye Contact: <strong>Optimal</strong></span>
                    </div>
                    <div className="hud-metric">
                      <ShieldCheck size={12} color="#10B981" />
                      <span>Posture: <strong>Centered</strong></span>
                    </div>
                  </div>
                </div>

                {/* Score telemetry preview */}
                <div className="laptop-mini-score-card">
                  <div className="score-row">
                    <span>Technical Depth</span>
                    <span className="score-val text-green">92%</span>
                  </div>
                  <div className="score-bar-bg">
                    <div className="score-bar-fill fill-92" />
                  </div>

                  <div className="score-row" style={{ marginTop: '8px' }}>
                    <span>Communication & Clarity</span>
                    <span className="score-val text-orange">88%</span>
                  </div>
                  <div className="score-bar-bg">
                    <div className="score-bar-fill fill-88" />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Laptop Bottom Base & Hinge */}
        <div className="laptop-base-chassis">
          <div className="laptop-hinge" />
          <div className="laptop-keyboard-deck">
            <div className="laptop-trackpad" />
          </div>
          <div className="laptop-front-lip" />
        </div>
      </div>
    </div>
  );
}
