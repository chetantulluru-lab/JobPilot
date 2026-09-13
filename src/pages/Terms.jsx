import React from 'react';
import SectionHeading from '../components/SectionHeading';
import GlassCard from '../components/GlassCard';
import GlowBackground from '../components/GlowBackground';
import { APP_CONFIG } from '../config/appConfig';

export default function Terms() {
  const sections = [
    {
      title: '1. Acceptance of Terms',
      content:
        'By downloading, accessing, or using the JobPilot website or Android mobile application, you agree to be bound by these Terms and Conditions. If you do not agree to these terms, do not access or use our services.',
    },
    {
      title: '2. Description of Service',
      content:
        'JobPilot is an AI and Natural Language Processing assisted career companion built for Android. The service provides resume text parsing, career profile structuring, semantic match scoring against job requirements, skill gap insights, and application workflow tracking.',
    },
    {
      title: '3. User Accounts',
      content:
        'To access certain features of the mobile application, you may be required to create an account. You are responsible for maintaining the confidentiality of your login credentials and for all activities that occur under your account.',
    },
    {
      title: '4. User-Provided Information',
      content:
        'You represent and warrant that all information you submit through JobPilot—including academic credentials, work history, skill proficiencies, and contact details—is accurate, truthful, and owned or licensed by you.',
    },
    {
      title: '5. Resume and Career Information',
      content:
        'You retain ownership of all resumes, documents, and career data uploaded to JobPilot. By uploading documents, you grant JobPilot a limited license to process, parse, and analyze your content solely to provide the services described.',
    },
    {
      title: '6. Job Listings and Third-Party Data',
      content:
        'Job descriptions and requirements displayed within JobPilot are sourced from third-party employers, public postings, or authorized feeds. JobPilot makes no representations or warranties regarding the accuracy, completeness, or ongoing availability of any listed position.',
    },
    {
      title: '7. Application Assistance & Non-Guarantee',
      content:
        'JobPilot provides assistance tools to help users prepare and organize application workflows. JobPilot does NOT guarantee interviews, employment, offers, salary levels, or successful applications. Hiring decisions remain solely within the discretion of prospective employers.',
    },
    {
      title: '8. Third-Party Platforms',
      content:
        'Our services may contain links or referral mechanisms to third-party websites, application tracking systems, or the Google Play Store. JobPilot does not endorse, control, or assume responsibility for the content, privacy policies, or practices of any third-party service.',
    },
    {
      title: '9. Acceptable Use',
      content:
        'You agree not to use JobPilot for any unlawful purpose, to reverse engineer any NLP model or application logic, to upload malicious software, or to attempt unauthorized access to our servers or network.',
    },
    {
      title: '10. Intellectual Property',
      content:
        'The JobPilot brand, logos, user interface designs, website source code, algorithms, and proprietary materials are the property of JobPilot and are protected by copyright and intellectual property laws. JobPilot does not claim registered trademark status.',
    },
    {
      title: '11. Disclaimers',
      content:
        'THE SERVICE IS PROVIDED ON AN "AS IS" AND "AS AVAILABLE" BASIS WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED. JOBPILOT DISCLAIMS ALL WARRANTIES, INCLUDING MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT.',
    },
    {
      title: '12. Limitation of Liability',
      content:
        'IN NO EVENT SHALL JOBPILOT, ITS CONTRIBUTORS, OR AFFILIATES BE LIABLE FOR ANY INDIRECT, INCIDENTAL, SPECIAL, CONSEQUENTIAL, OR PUNITIVE DAMAGES ARISING OUT OF OR IN CONNECTION WITH YOUR ACCESS OR USE OF THE SERVICE.',
    },
    {
      title: '13. Changes to Terms',
      content:
        'We reserve the right to modify these Terms at any time. Any changes will become effective immediately upon posting to this website. Continued use of JobPilot after such modifications signifies your acceptance.',
    },
    {
      title: '14. Contact',
      content: `For any inquiries or notices regarding these Terms, please reach out via our contact page or email us at ${APP_CONFIG.supportEmail}.`,
    },
  ];

  return (
    <div className="page-terms">
      <section className="section" style={{ paddingTop: '60px', paddingBottom: '90px', position: 'relative' }}>
        <GlowBackground variant="top" />

        <div className="container container-narrow">
          <SectionHeading
            eyebrow="User Agreement"
            title="Terms &"
            highlight="Conditions"
            subtitle="Last updated: September 2026 • Official Terms of Use"
          />

          {/* Prominent Mandatory Disclaimer Banner */}
          <div
            style={{
              padding: '18px 24px',
              borderRadius: 'var(--radius-md)',
              background: 'linear-gradient(135deg, #FFF5EB 0%, #FFE9D6 100%)',
              border: '1px solid var(--orange-300)',
              fontSize: '0.9375rem',
              color: 'var(--orange-800)',
              marginBottom: '36px',
              lineHeight: '1.6',
            }}
          >
            <strong>Important Employment Disclaimer:</strong> JobPilot does not guarantee interviews, employment, offers, or successful applications. JobPilot is an AI and natural language processing tool designed to assist with career organization and compatibility insights.
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
