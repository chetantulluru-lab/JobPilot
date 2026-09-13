import React, { useState, useEffect } from 'react';
import { NavLink, Link, useLocation } from 'react-router-dom';
import { Menu, X, ArrowRight, Smartphone } from 'lucide-react';
import Button from './Button';
import ApkDownloadButton from './ApkDownloadButton';

export default function Navbar() {
  const [isScrolled, setIsScrolled] = useState(false);
  const [isMobileOpen, setIsMobileOpen] = useState(false);
  const location = useLocation();
  const [prevPath, setPrevPath] = useState(location.pathname);

  if (prevPath !== location.pathname) {
    setPrevPath(location.pathname);
    setIsMobileOpen(false);
  }

  // Scroll listener for sticky translucency
  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY > 20) {
        setIsScrolled(true);
      } else {
        setIsScrolled(false);
      }
    };

    window.addEventListener('scroll', handleScroll, { passive: true });
    handleScroll();
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const navLinks = [
    { label: 'Features', path: '/features' },
    { label: 'How It Works', path: '/how-it-works' },
    { label: 'About', path: '/about' },
    { label: 'Download', path: '/download' },
  ];

  return (
    <header className={`navbar-wrapper ${isScrolled ? 'scrolled' : ''}`}>
      <div className="navbar-inner">
        {/* Brand Left */}
        <Link to="/" className="nav-brand" aria-label="JobPilot Home">
          <span>JobPilot</span>
          <span className="nav-brand-symbol">✦</span>
        </Link>

        {/* Center Navigation Links (Desktop) */}
        <nav aria-label="Main Navigation">
          <ul className="nav-links-desktop">
            {navLinks.map((link) => (
              <li key={link.path}>
                <NavLink
                  to={link.path}
                  className={({ isActive }) =>
                    `nav-link-item ${isActive ? 'active' : ''}`
                  }
                >
                  {link.label}
                </NavLink>
              </li>
            ))}
          </ul>
        </nav>

        {/* Right Actions (Desktop) */}
        <div className="nav-actions-desktop">
          <NavLink
            to="/contact"
            className={({ isActive }) =>
              `nav-link-item ${isActive ? 'active' : ''}`
            }
          >
            Contact
          </NavLink>
          <Button
            to="/download"
            variant="primary"
            size="sm"
            icon={<ArrowRight size={15} />}
          >
            Get the App
          </Button>
        </div>

        {/* Mobile Hamburger Button */}
        <button
          className="mobile-toggle-btn"
          onClick={() => setIsMobileOpen((prev) => !prev)}
          aria-label={isMobileOpen ? 'Close Navigation Menu' : 'Open Navigation Menu'}
          aria-expanded={isMobileOpen}
        >
          {isMobileOpen ? <X size={22} /> : <Menu size={22} />}
        </button>
      </div>

      {/* Mobile Drawer (Slide & Fade) */}
      <div
        className={`mobile-nav-drawer ${isMobileOpen ? 'open' : ''}`}
        aria-hidden={!isMobileOpen}
      >
        <ul className="mobile-nav-links">
          <li>
            <NavLink
              to="/"
              className={({ isActive }) =>
                `mobile-nav-link ${isActive ? 'active' : ''}`
              }
            >
              Home
            </NavLink>
          </li>
          {navLinks.map((link) => (
            <li key={link.path}>
              <NavLink
                to={link.path}
                className={({ isActive }) =>
                  `mobile-nav-link ${isActive ? 'active' : ''}`
                }
              >
                {link.label}
              </NavLink>
            </li>
          ))}
          <li>
            <NavLink
              to="/contact"
              className={({ isActive }) =>
                `mobile-nav-link ${isActive ? 'active' : ''}`
              }
            >
              Contact
            </NavLink>
          </li>
        </ul>

        <div className="mobile-nav-actions">
          <ApkDownloadButton size="md" style={{ width: '100%', justifyContent: 'center' }} />
          <Button
            to="/download"
            variant="secondary"
            size="md"
            icon={<Smartphone size={18} />}
            iconPosition="left"
            style={{ width: '100%' }}
          >
            Download Overview
          </Button>
        </div>
      </div>
    </header>
  );
}
