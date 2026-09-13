import React, { Suspense, lazy } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import ScrollToTop from './components/ScrollToTop';
import Button from './components/Button';
import GlassCard from './components/GlassCard';

// Route-based code splitting for peak performance
const Home = lazy(() => import('./pages/Home'));
const Features = lazy(() => import('./pages/Features'));
const HowItWorks = lazy(() => import('./pages/HowItWorks'));
const About = lazy(() => import('./pages/About'));
const Download = lazy(() => import('./pages/Download'));
const Privacy = lazy(() => import('./pages/Privacy'));
const Terms = lazy(() => import('./pages/Terms'));
const Contact = lazy(() => import('./pages/Contact'));

function PageLoader() {
  return (
    <div
      style={{
        minHeight: '60vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        flexDirection: 'column',
        gap: '16px',
      }}
    >
      <div
        style={{
          width: '42px',
          height: '42px',
          borderRadius: '50%',
          border: '3px solid var(--orange-100)',
          borderTopColor: 'var(--orange-500)',
          animation: 'orbFloat 1s linear infinite',
        }}
      />
      <span style={{ fontSize: '0.875rem', color: 'var(--text-muted)', fontFamily: 'var(--font-display)', fontWeight: '600' }}>
        JobPilot ✦
      </span>
    </div>
  );
}

function NotFound() {
  return (
    <div className="section" style={{ minHeight: '60vh', display: 'flex', alignItems: 'center' }}>
      <div className="container" style={{ textAlign: 'center' }}>
        <GlassCard elevated padding="clamp(24px, 5vw, 48px)" style={{ maxWidth: '520px', margin: '0 auto' }}>
          <div style={{ fontSize: '3rem', fontWeight: '800', color: 'var(--orange-500)', marginBottom: '12px' }}>
            404
          </div>
          <h2 style={{ fontSize: '1.5rem', fontWeight: '800', marginBottom: '12px' }}>
            Page Not Found
          </h2>
          <p style={{ color: 'var(--text-secondary)', marginBottom: '24px' }}>
            The career coordinate you are looking for does not exist or has moved.
          </p>
          <Button to="/" variant="primary">
            Return to JobPilot Home
          </Button>
        </GlassCard>
      </div>
    </div>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <ScrollToTop />
      <div className="app-layout">
        <Navbar />
        <main style={{ flexGrow: 1 }}>
          <Suspense fallback={<PageLoader />}>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/features" element={<Features />} />
              <Route path="/how-it-works" element={<HowItWorks />} />
              <Route path="/about" element={<About />} />
              <Route path="/download" element={<Download />} />
              <Route path="/privacy" element={<Privacy />} />
              <Route path="/terms" element={<Terms />} />
              <Route path="/contact" element={<Contact />} />
              <Route path="*" element={<NotFound />} />
            </Routes>
          </Suspense>
        </main>
        <Footer />
      </div>
    </BrowserRouter>
  );
}
