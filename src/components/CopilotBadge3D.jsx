import React, { useState } from 'react';
import { Sparkles, Bot, Video, Zap, Award, CheckCircle2 } from 'lucide-react';

/**
 * 3D Interactive Copilot Avatar Badge
 * A futuristic 3D holographic avatar unit with multi-axis tilt,
 * energetic neural particles, and feature telemetry.
 */
export default function CopilotBadge3D({ className = '' }) {
  const [tilt, setTilt] = useState({ x: 0, y: 0 });
  const [isHovered, setIsHovered] = useState(false);

  const handleMouseMove = (e) => {
    const rect = e.currentTarget.getBoundingClientRect();
    const x = e.clientX - rect.left - rect.width / 2;
    const y = e.clientY - rect.top - rect.height / 2;
    // Normalized tilt
    setTilt({
      x: -(y / (rect.height / 2)) * 12,
      y: (x / (rect.width / 2)) * 12,
    });
  };

  const handleMouseLeave = () => {
    setTilt({ x: 0, y: 0 });
    setIsHovered(false);
  };

  return (
    <div
      className={`copilot-3d-wrapper ${className}`}
      onMouseMove={handleMouseMove}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={handleMouseLeave}
      style={{
        transform: `perspective(1000px) rotateX(${tilt.x}deg) rotateY(${tilt.y}deg) scale(${isHovered ? 1.02 : 1})`,
        transition: isHovered ? 'transform 0.1s ease-out' : 'transform 0.5s cubic-bezier(0.16, 1, 0.3, 1)',
      }}
    >
      {/* 3D Ambient Glow Core */}
      <div className="copilot-glow-core" />

      {/* Holographic Frame */}
      <div className="copilot-card-surface">
        {/* Top Header Badge */}
        <div className="copilot-top-meta">
          <div className="copilot-status-dot">
            <span className="dot-ping" />
            <span className="dot-solid" />
          </div>
          <span className="copilot-version-tag">JobPilot AI Copilot v2.0 • 3D Engine</span>
        </div>

        {/* 3D Avatar Centerpiece */}
        <div className="copilot-avatar-stage">
          <div className="copilot-ring-orbit orbit-outer" />
          <div className="copilot-ring-orbit orbit-inner" />
          
          <div className="copilot-avatar-orb">
            <div className="avatar-core-icon">
              <Bot size={36} color="#FFFFFF" />
            </div>
            <div className="avatar-hologram-shimmer" />
          </div>

          <div className="copilot-orbit-chip chip-left">
            <Video size={13} color="#FF6A00" />
            <span>Face AI</span>
          </div>

          <div className="copilot-orbit-chip chip-right">
            <Zap size={13} color="#10B981" />
            <span>ATS 98%</span>
          </div>
        </div>

        {/* Copilot Capability Stats */}
        <div className="copilot-footer-metrics">
          <div className="metric-chip">
            <CheckCircle2 size={13} color="#10B981" />
            <span>100% Free OTP Auth</span>
          </div>
          <div className="metric-chip">
            <Award size={13} color="#FF6A00" />
            <span>5 New AI Engines</span>
          </div>
        </div>
      </div>
    </div>
  );
}
