import React from 'react';
import SectionHeading from '../components/SectionHeading';
import GlassCard from '../components/GlassCard';
import GlowBackground from '../components/GlowBackground';
import { APP_CONFIG } from '../config/appConfig';

export default function Privacy() {
  const sections = [
    {
      title: '1. Information We May Collect',
      content:
        'JobPilot collects information provided directly by users when creating an account, uploading a resume, or interacting with the Android mobile application. This includes profile metadata (such as name and email), self-reported education history, technical skills, project links, and job search preferences.',
    },
    {
      title: '2. How Information Is Used',
      content:
        'We use collected information solely to power the JobPilot features: converting resume documents into structured data models, calculating semantic compatibility scores with job descriptions, diagnosing skill gaps, and assisting with application organization. We do not sell personal data to third parties or data brokers.',
    },
    {
      title: '3. Resume and Career Information',
      content:
        'When you upload a resume document (such as a PDF or DOCX file), JobPilot processes the document using Natural Language Processing (NLP) routines to identify relevant entities (skills, educational institutions, dates, employment history). The processed data is stored as a structured career profile linked to your user account.',
    },
    {
      title: '4. Account Information',
      content:
        'Account credentials and basic identifiers are maintained to secure your personal career profile and synchronize your preferences across Android app sessions. You maintain the ability to update, edit, or delete profile fields at any time.',
    },
    {
      title: '5. Device Information',
      content:
        'To ensure reliable operation on Android devices, the application may record non-personally identifiable diagnostic data including Android OS version, device model, app crash logs, and network performance indicators.',
    },
    {
      title: '6. Data Security',
      content:
        'JobPilot employs industry-standard encryption protocols for data in transit (HTTPS / TLS) and standard safeguards for stored data. However, no method of digital transmission or storage is 100% impenetrable, and users are encouraged to maintain unique credentials.',
    },
    {
      title: '7. Third-Party Services',
      content:
        'JobPilot may integrate with third-party infrastructure providers such as cloud hosting services, NLP API engines, and Google Play Store services. These third parties access user data only to execute contracted technical services and under confidentiality commitments.',
    },
    {
      title: '8. Data Retention',
      content:
        'We retain your career profile and application history for as long as your account remains active or as needed to provide you with service. You may request deletion of your account and associated resume records at any time.',
    },
    {
      title: '9. User Rights',
      content:
        'Depending on your jurisdiction, you may have the right to access, rectify, export, or delete your personal data. You can exercise these options through in-app settings or by contacting our team.',
    },
    {
      title: '10. Children’s Privacy',
      content:
        'JobPilot is designed for university students, college attendees, and adult career seekers. It is not intended for use by individuals under the age of 16, and we do not knowingly gather data from children.',
    },
    {
      title: '11. Changes to This Policy',
      content:
        'We may update this Privacy Policy from time to time to reflect modifications in our features or legal requirements. Updated policies will be posted on this page with an updated revision date.',
    },
    {
      title: '12. Contact',
      content: `If you have questions regarding this Privacy Policy or your personal information, please contact us via our contact page or email us at ${APP_CONFIG.supportEmail}.`,
    },
  ];

  return (
    <div className="page-privacy">
      <section className="section" style={{ paddingTop: '60px', paddingBottom: '90px', position: 'relative' }}>
        <GlowBackground variant="top" />

        <div className="container container-narrow">
          <SectionHeading
            eyebrow="Legal & Transparency"
            title="JobPilot"
            highlight="Privacy Policy"
            subtitle="Last updated: September 2026 • Official Privacy Notice"
          />

          {/* Project Stage Notice */}
          <div
            style={{
              padding: '16px 20px',
              borderRadius: 'var(--radius-md)',
              background: 'var(--orange-50)',
              border: '1px solid var(--orange-200)',
              fontSize: '0.875rem',
              color: 'var(--orange-800)',
              marginBottom: '36px',
              lineHeight: '1.6',
            }}
          >
            <strong>Note on Project Stage:</strong> This is a project-stage privacy policy template structured for transparency and user clarity. Prior to full public production deployment, it will be reviewed and finalized by legal counsel in accordance with applicable data protection regulations.
          </div>

          <GlassCard elevated padding="clamp(20px, 4vw, 40px)">
            <div style={{ display: 'flex', flexDirection: 'column', gap: '32px' }}>
              {sections.map((sec, idx) => (
                <div key={idx} style={{ borderBottom: idx < sections.length - 1 ? '1px solid rgba(226, 232, 240, 0.7)' : 'none', paddingBottom: '24px' }}>
                  <h3
                    style={{
                      fontSize: '1.25rem',
                      fontWeight: '800',
                      color: 'var(--text-primary)',
                      marginBottom: '10px',
                    }}
                  >
                    {sec.title}
                  </h3>
                  <p style={{ fontSize: '0.9375rem', color: 'var(--text-secondary)', lineHeight: '1.7' }}>
                    {sec.content}
                  </p>
                </div>
              ))}
            </div>
          </GlassCard>
        </div>
      </section>
    </div>
  );
}
