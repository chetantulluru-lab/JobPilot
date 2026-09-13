import SectionHeading from '../components/SectionHeading';
import GlassCard from '../components/GlassCard';
import GlowBackground from '../components/GlowBackground';
import PlayStoreButton from '../components/PlayStoreButton';

export default function About() {
  const problems = [
    'Maintaining and tweaking dozens of separate resume formats for every role.',
    'Repeatedly typing the same contact, education, and project details into endless application portals.',
    'Reading through dense, ambiguous job descriptions that obscure what the company actually needs.',
    'Guessing whether your background is a competitive match or if you are missing a critical requirement.',
    'Losing track of application dates, recruiter touchpoints, and follow-up deadlines across multiple platforms.',
  ];

  const pillars = [
    {
      title: 'Discover',
      desc: 'Surfacing opportunities where your real-world skills and projects genuinely align.',
    },
    {
      title: 'Understand',
      desc: 'Explaining match percentages with transparent strong matches and skill gap diagnoses.',
    },
    {
      title: 'Manage',
      desc: 'Keeping your career profile, documents, and application tracking organized in one mobile hub.',
    },
  ];

  return (
    <div className="page-about">
      {/* Header */}
      <section className="section" style={{ paddingTop: '60px', paddingBottom: '50px', position: 'relative' }}>
        <GlowBackground variant="top" />
        <div className="container">
          <SectionHeading
            eyebrow="Our Mission & Purpose"
            title="We're building a smarter way to"
            highlight="navigate the job search."
            subtitle="JobPilot was conceived to remove the friction, opacity, and cognitive overload from early-career hiring."
          />
        </div>
      </section>

      {/* The Problem Section */}
      <section className="section" style={{ paddingTop: '0', paddingBottom: '70px' }}>
        <div className="container">
          <div
            style={{
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))',
              gap: '40px',
              alignItems: 'center',
            }}
          >
            <div>
              <span className="badge badge-orange" style={{ marginBottom: '14px' }}>
                The Friction in Job Hunting
              </span>
              <h3 style={{ fontSize: '2rem', fontWeight: '800', marginBottom: '18px', lineHeight: '1.2' }}>
                Searching for opportunities has become repetitive and overwhelming.
              </h3>
              <p style={{ color: 'var(--text-secondary)', lineHeight: '1.7', marginBottom: '20px' }}>
                For students and early career professionals, finding an internship or full-time software position often turns into an exhausting administrative chore rather than an inspiring career milestone.
              </p>
              <p style={{ color: 'var(--text-secondary)', lineHeight: '1.7' }}>
                Candidates spend more time re-entering basic biographical information and guessing what automated applicant tracking systems look for than sharpening their technical craft.
              </p>
            </div>

            <GlassCard elevated padding="32px">
              <h4 style={{ fontSize: '1.125rem', fontWeight: '700', marginBottom: '16px', color: 'var(--text-primary)' }}>
                The Repetitive Hurdles Candidates Face:
              </h4>
              <ul style={{ listStyle: 'none', display: 'flex', flexDirection: 'column', gap: '12px' }}>
                {problems.map((problem, idx) => (
                  <li key={idx} style={{ display: 'flex', alignItems: 'flex-start', gap: '10px' }}>
                    <span
                      style={{
                        width: '20px',
                        height: '20px',
                        borderRadius: '50%',
                        background: '#FEE2E2',
                        color: '#DC2626',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontSize: '0.75rem',
                        fontWeight: '700',
                        flexShrink: 0,
                        marginTop: '2px',
                      }}
                    >
                      !
                    </span>
                    <span style={{ fontSize: '0.9375rem', color: 'var(--text-secondary)', lineHeight: '1.5' }}>
                      {problem}
                    </span>
                  </li>
                ))}
              </ul>
            </GlassCard>
          </div>
        </div>
      </section>

      {/* The Mission Section */}
      <section
        className="section"
        style={{
          background: 'linear-gradient(180deg, rgba(255, 255, 255, 0) 0%, rgba(255, 247, 237, 0.6) 50%, rgba(255, 255, 255, 0) 100%)',
        }}
      >
        <div className="container">
          <div style={{ maxWidth: '840px', margin: '0 auto', textAlign: 'center' }}>
            <span className="badge badge-orange" style={{ marginBottom: '16px' }}>
              ✦ The JobPilot Mission
            </span>
            <h2
              style={{
                fontSize: 'clamp(2.125rem, 3.5vw, 3rem)',
                fontWeight: '800',
                letterSpacing: '-0.03em',
                lineHeight: '1.2',
                marginBottom: '24px',
              }}
            >
              Make career opportunities easier to{' '}
              <span className="text-gradient-orange">discover, understand and manage.</span>
            </h2>
            <p
              style={{
                fontSize: '1.125rem',
                color: 'var(--text-secondary)',
                lineHeight: '1.7',
                marginBottom: '40px',
              }}
            >
              JobPilot is designed to bring intelligence into this process. By combining natural language processing with structured candidate modeling on Android, we help applicants navigate the modern job market with actionable insights and zero guesswork.
            </p>

            <div
              style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))',
                gap: '20px',
                textAlign: 'left',
              }}
            >
              {pillars.map((pillar, idx) => (
                <GlassCard key={idx} padding="24px" interactive>
                  <div
                    style={{
                      fontSize: '1.25rem',
                      fontWeight: '800',
                      color: 'var(--orange-600)',
                      marginBottom: '8px',
                    }}
                  >
                    0{idx + 1}. {pillar.title}
                  </div>
                  <p style={{ fontSize: '0.875rem', color: 'var(--text-secondary)', lineHeight: '1.6' }}>
                    {pillar.desc}
                  </p>
                </GlassCard>
              ))}
            </div>
          </div>
        </div>
      </section>

      {/* Honest Scope & Architecture Note */}
      <section className="section" style={{ paddingBottom: '90px' }}>
        <div className="container">
          <div style={{ maxWidth: '780px', margin: '0 auto' }}>
            <GlassCard padding="32px">
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '12px' }}>
                <span className="badge badge-glass">Project Principles</span>
              </div>
              <h4 style={{ fontSize: '1.125rem', fontWeight: '700', marginBottom: '10px' }}>
                Honest Product Scope
              </h4>
              <p style={{ fontSize: '0.9375rem', color: 'var(--text-secondary)', lineHeight: '1.6', marginBottom: '20px' }}>
                JobPilot is designed as an AI and natural language processing career assistant. We do not guarantee job interviews, employment offers, or automated acceptance. Our goal is to empower users with clear data, semantic matching insights, and organized workflows so they can put their best foot forward.
              </p>
              <div style={{ display: 'flex', justifyContent: 'flex-start' }}>
                <PlayStoreButton size="sm" />
              </div>
            </GlassCard>
          </div>
        </div>
      </section>
    </div>
  );
}
