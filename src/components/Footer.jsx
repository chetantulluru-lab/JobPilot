import React from 'react';
import { Link } from 'react-router-dom';
import { APP_CONFIG } from '../config/appConfig';
import PlayStoreButton from './PlayStoreButton';
import ApkDownloadButton from './ApkDownloadButton';

export default function Footer() {
  return (
    <footer className="footer-wrapper">
      <div className="container">
        <div className="footer-grid">
          {/* Brand Col */}
          <div>
            <Link to="/" className="nav-brand" style={{ marginBottom: '12px', display: 'inline-flex' }}>
              <span>JobPilot</span>
              <span className="nav-brand-symbol">✦</span>
            </Link>
            <p style={{ fontSize: '0.9375rem', color: 'var(--text-secondary)', marginBottom: '16px', maxWidth: '280px' }}>
              {APP_CONFIG.tagline}
            </p>
            <div style={{ fontSize: '0.8125rem', color: 'var(--text-muted)', lineHeight: '1.5' }}>
              Empowering students and early-career engineers with NLP-driven job discovery and structured career intelligence on Android.
            </div>
          </div>

          {/* Product Links */}
          <div>
            <h4 className="footer-heading">Product</h4>
            <ul className="footer-links-list">
              <li>
                <Link to="/features" className="footer-link">
                  Features
                </Link>
              </li>
              <li>
                <Link to="/how-it-works" className="footer-link">
                  How It Works
                </Link>
              </li>
              <li>
                <Link to="/download" className="footer-link">
                  Download
                </Link>
              </li>
            </ul>
          </div>

          {/* Company Links */}
          <div>
            <h4 className="footer-heading">Company</h4>
            <ul className="footer-links-list">
              <li>
                <Link to="/about" className="footer-link">
                  About
                </Link>
              </li>
              <li>
                <Link to="/contact" className="footer-link">
                  Contact
                </Link>
              </li>
            </ul>
          </div>

          {/* Legal Links */}
          <div>
            <h4 className="footer-heading">Legal</h4>
            <ul className="footer-links-list">
              <li>
                <Link to="/privacy" className="footer-link">
                  Privacy Policy
                </Link>
              </li>
              <li>
                <Link to="/terms" className="footer-link">
                  Terms & Conditions
                </Link>
              </li>
            </ul>
          </div>

          {/* Mobile Col */}
          <div>
            <h4 className="footer-heading">Mobile</h4>
            <p style={{ fontSize: '0.8125rem', color: 'var(--text-muted)', marginBottom: '14px' }}>
              Built specifically for Android. Experience career intelligence in your pocket.
            </p>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', alignItems: 'flex-start' }}>
              <ApkDownloadButton size="sm" />
              <PlayStoreButton size="sm" />
            </div>
            <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginTop: '8px' }}>
              {APP_CONFIG.minAndroidVersion}
            </div>
          </div>
        </div>

        {/* Bottom Bar */}
        <div className="footer-bottom">
          <div>
            © 2026 JobPilot. All rights reserved.
          </div>
          <div style={{ fontSize: '0.8125rem', color: 'var(--text-faint)' }}>
            JobPilot does not guarantee employment, interviews, or job offers. All career matching is advisory and powered by natural language processing.
          </div>
        </div>
      </div>
    </footer>
  );
}
