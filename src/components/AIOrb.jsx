import React from 'react';
import { Sparkles, CheckCircle2, TrendingUp, Briefcase, Zap } from 'lucide-react';

/**
 * Signature JobPilot AI Orb & Core Visual
 * Generates an animated, multi-layered floating AI orb with orange ambient glow,
 * white radiating core, orbiting particle tracks, and floating career intelligence glass cards.
 */
export default function AIOrb({ className = '', showCards = true }) {
  return (
    <div className={`ai-orb-container ${className}`} aria-label="JobPilot Intelligent AI Core">
      {/* Ambient background blur glow */}
      <div className="ai-orb-ambient-glow" />

      {/* Orbiting concentric subtle geometric rings */}
      <div className="ai-orb-ring ai-orb-ring-1" />
      <div className="ai-orb-ring ai-orb-ring-2" />

      {/* Main floating orb core wrapper */}
      <div className="ai-orb-core-wrapper">
        <div className="ai-orb-sphere">
          {/* Inner radial gradient gives a glowing white center transitioning into warm orange */}
        </div>

        {/* Central Rotating AI Sparkle */}
        <div className="ai-orb-sparkle-center" aria-hidden="true">
          ✦
        </div>
      </div>

      {/* Floating Glass Information Cards (Subtly animated around orb) */}
      {showCards && (
        <>
          {/* Card 1: 94% Job Match */}
          <div className="floating-card floating-card-1" style={{ borderLeft: '3px solid #10B981' }}>
            <span style={{ color: '#10B981', display: 'flex' }}>
              <CheckCircle2 size={16} />
            </span>
            <span>
              <strong style={{ color: '#0F172A' }}>94%</strong> Job Match
            </span>
          </div>

          {/* Card 2: Python Developer */}
          <div className="floating-card floating-card-2" style={{ borderLeft: '3px solid #FF6A00' }}>
            <span style={{ color: '#FF6A00', display: 'flex' }}>
              <Briefcase size={16} />
            </span>
            <span>
              <strong>Python</strong> Developer
            </span>
          </div>

          {/* Card 3: 3 new opportunities */}
          <div className="floating-card floating-card-3" style={{ borderLeft: '3px solid #3B82F6' }}>
            <span style={{ color: '#3B82F6', display: 'flex' }}>
              <Zap size={16} />
            </span>
            <span>
              <strong style={{ color: '#0F172A' }}>3 new</strong> opportunities
            </span>
          </div>

          {/* Card 4: Profile Strength 86% */}
          <div className="floating-card floating-card-4" style={{ borderLeft: '3px solid #F59E0B' }}>
            <span style={{ color: '#F59E0B', display: 'flex' }}>
              <TrendingUp size={16} />
            </span>
            <span>
              Profile Strength <strong>86%</strong>
            </span>
          </div>

          {/* Card 5: AI Career Insight */}
          <div className="floating-card floating-card-5" style={{ borderLeft: '3px solid #8B5CF6' }}>
            <span style={{ color: '#8B5CF6', display: 'flex' }}>
              <Sparkles size={16} />
            </span>
            <span>
              AI Career <strong>Insight</strong>
            </span>
          </div>
        </>
      )}
    </div>
  );
}
