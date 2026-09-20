import React, { useState } from 'react';
import { 
  PlayCircle, 
  Flame, 
  Bookmark, 
  BookmarkCheck, 
  CheckCircle2, 
  Save, 
  Check
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function ClientRoadmap() {
  const { user, updateUser } = useAuth();
  const [selectedDay, setSelectedDay] = useState(14);
  const [activeTab, setActiveTab] = useState('video'); // 'video' | 'quiz' | 'notes'
  const [selectedLang, setSelectedLang] = useState('en'); // 'en' | 'te' | 'hi'
  const [isBookmarked, setIsBookmarked] = useState(false);
  const [noteContent, setNoteContent] = useState(
    'Key takeaway: Always use Redis distributed lock with lease expiration when multiple workers handle shared payment order queues to prevent deadlocks.'
  );
  const [noteSaved, setNoteSaved] = useState(false);

  // Quiz State
  const [quizAnswers, setQuizAnswers] = useState({});
  const [quizSubmitted, setQuizSubmitted] = useState(false);
  const [quizScore, setQuizScore] = useState(0);

  const daysList = [
    { day: 12, title: 'PostgreSQL Relational Schemas & Indexes', completed: true },
    { day: 13, title: 'FastAPI Dependency Injection & Middleware', completed: true },
    { day: 14, title: 'Asynchronous Workers & Redis Concurrency', completed: false, active: true },
    { day: 15, title: 'Docker Containerization & Microservice Networks', completed: false },
    { day: 16, title: 'JWT Authentication & OAuth2 Provider Integration', completed: false },
  ];

  const quizQuestions = [
    {
      id: 1,
      question: 'Which Redis data structure is best suited for an asynchronous task queue?',
      options: ['Sorted Set (ZSET)', 'List (LPUSH / BRPOP)', 'Hash Map (HSET)', 'HyperLogLog'],
      correct: 1,
      explanation: 'Redis Lists with LPUSH and BRPOP provide FIFO reliable message queuing semantics.',
    },
    {
      id: 2,
      question: 'Why should a distributed lock always have an expiration lease time (TTL)?',
      options: [
        'To reduce RAM usage on Redis',
        'To prevent infinite deadlocks if the holding worker crashes unexpectedly',
        'To make the lock accessible to unauthorized users',
        'To automatically serialize all incoming HTTP requests',
      ],
      correct: 1,
      explanation: 'A TTL guarantees that if the worker holding the lock fails or disconnects, the lock will automatically release.',
    },
    {
      id: 3,
      question: 'What is the primary benefit of running async worker tasks outside the main FastAPI process?',
      options: [
        'Decreases total lines of code',
        'Prevents long-running computations from blocking the async event loop',
        'Eliminates the need for a database',
        'Increases network bandwidth automatically',
      ],
      correct: 1,
      explanation: 'Heavy I/O and CPU jobs delegated to worker pools prevent starving the main asyncio event loop.',
    },
  ];

  const handleSelectOption = (qIdx, optIdx) => {
    if (quizSubmitted) return;
    setQuizAnswers((prev) => ({ ...prev, [qIdx]: optIdx }));
  };

  const submitQuiz = () => {
    let score = 0;
    quizQuestions.forEach((q, idx) => {
      if (quizAnswers[idx] === q.correct) {
        score += 1;
      }
    });
    setQuizScore(score);
    setQuizSubmitted(true);
    if (score >= 2) {
      updateUser({ streak: (user?.streak || 3) + 1 });
    }
  };

  const handleSaveNote = () => {
    setNoteSaved(true);
    setTimeout(() => setNoteSaved(false), 2000);
  };

  // Video embed mappings
  const videoUrls = {
    en: 'https://www.youtube.com/embed/gQliT_l6c9A',
    te: 'https://www.youtube.com/embed/gQliT_l6c9A',
    hi: 'https://www.youtube.com/embed/gQliT_l6c9A',
  };

  return (
    <div style={{ maxWidth: '1180px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '24px' }}>
      {/* Track Header */}
      <div className="client-card client-card-glow" style={{ padding: '24px 28px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '12px' }}>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '6px' }}>
              <span className="client-badge client-badge-orange">6-Month Curriculum</span>
              <span className="client-badge client-badge-blue">Day {selectedDay}</span>
            </div>
            <h1 style={{ fontSize: '1.6rem', fontWeight: 800, margin: '0 0 4px 0' }}>
              Day {selectedDay}: Asynchronous Workers & Redis Concurrency
            </h1>
            <p style={{ color: 'var(--app-text-secondary)', margin: 0, fontSize: '0.875rem' }}>
              Track: Python Backend Developer & Microservices Architecture
            </p>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <button
              onClick={() => setIsBookmarked(!isBookmarked)}
              className="client-btn client-btn-secondary"
              style={{ padding: '8px 14px', fontSize: '0.8125rem' }}
            >
              {isBookmarked ? <BookmarkCheck size={16} color="#10B981" /> : <Bookmark size={16} />}
              <span>{isBookmarked ? 'Bookmarked' : 'Bookmark Day'}</span>
            </button>
          </div>
        </div>
      </div>

      {/* Main Learning Hub Split: Days List (Left) + Content (Right) */}
      <div style={{ display: 'grid', gridTemplateColumns: '280px 1fr', gap: '20px', alignItems: 'start' }}>
        {/* Left: Curriculum Day List */}
        <div className="client-card" style={{ padding: '16px', display: 'flex', flexDirection: 'column', gap: '8px' }}>
          <div style={{ fontSize: '0.8125rem', fontWeight: 800, color: 'var(--app-text-secondary)', marginBottom: '8px', textTransform: 'uppercase' }}>
            Curriculum Schedule
          </div>
          {daysList.map((item) => (
            <button
              key={item.day}
              onClick={() => {
                setSelectedDay(item.day);
                setQuizSubmitted(false);
                setQuizAnswers({});
              }}
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: '10px',
                padding: '10px 12px',
                borderRadius: '8px',
                background: item.day === selectedDay ? 'rgba(255, 106, 0, 0.15)' : 'transparent',
                border: item.day === selectedDay ? '1px solid rgba(255, 106, 0, 0.3)' : '1px solid transparent',
                color: item.day === selectedDay ? '#FFFFFF' : 'var(--app-text-secondary)',
                cursor: 'pointer',
                textAlign: 'left',
                width: '100%',
              }}
            >
              <div style={{ color: item.completed ? '#10B981' : item.day === selectedDay ? 'var(--app-orange)' : '#64748B' }}>
                {item.completed ? <CheckCircle2 size={16} /> : <PlayCircle size={16} />}
              </div>
              <div style={{ flex: 1 }}>
                <div style={{ fontSize: '0.75rem', fontWeight: 700 }}>Day {item.day}</div>
                <div style={{ fontSize: '0.6875rem', color: 'var(--app-text-muted)', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', maxWidth: '180px' }}>
                  {item.title}
                </div>
              </div>
            </button>
          ))}
        </div>

        {/* Right: Tabbed Content Container */}
        <div className="client-card" style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {/* Navigation Tabs */}
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '12px' }}>
            <div className="client-tabs">
              <button
                onClick={() => setActiveTab('video')}
                className={`client-tab-btn ${activeTab === 'video' ? 'active' : ''}`}
              >
                📹 Video Lesson
              </button>
              <button
                onClick={() => setActiveTab('quiz')}
                className={`client-tab-btn ${activeTab === 'quiz' ? 'active' : ''}`}
              >
                🏆 Daily Quiz ({quizSubmitted ? `${quizScore}/3` : 'Pending'})
              </button>
              <button
                onClick={() => setActiveTab('notes')}
                className={`client-tab-btn ${activeTab === 'notes' ? 'active' : ''}`}
              >
                📝 My Notes
              </button>
            </div>

            {/* Language Switcher (for Video Tab) */}
            {activeTab === 'video' && (
              <div style={{ display: 'flex', gap: '6px' }}>
                {['en', 'te', 'hi'].map((lang) => (
                  <button
                    key={lang}
                    onClick={() => setSelectedLang(lang)}
                    className="client-btn"
                    style={{
                      padding: '4px 10px',
                      fontSize: '0.75rem',
                      background: selectedLang === lang ? 'var(--app-orange)' : 'rgba(255, 255, 255, 0.05)',
                      color: '#FFFFFF',
                    }}
                  >
                    {lang === 'en' ? 'English' : lang === 'te' ? 'తెలుగు' : 'हिन्दी'}
                  </button>
                ))}
              </div>
            )}
          </div>

          {/* TAB 1: VIDEO LESSON */}
          {activeTab === 'video' && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              <div style={{ position: 'relative', width: '100%', aspectRatio: '16 / 9', borderRadius: '12px', overflow: 'hidden', background: '#000000' }}>
                <iframe
                  src={videoUrls[selectedLang]}
                  title="Lesson Video"
                  style={{ width: '100%', height: '100%', border: 'none' }}
                  allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                  allowFullScreen
                />
              </div>

              <div style={{ background: 'rgba(255, 255, 255, 0.03)', padding: '16px', borderRadius: '10px' }}>
                <h4 style={{ fontSize: '0.9375rem', fontWeight: 800, margin: '0 0 8px 0' }}>
                  Key Lesson Takeaways:
                </h4>
                <ul style={{ margin: 0, paddingLeft: '20px', fontSize: '0.8125rem', color: 'var(--app-text-secondary)', lineHeight: 1.6 }}>
                  <li>Asynchronous worker queues allow offloading database exports, emails, and compute workloads.</li>
                  <li>Use Redis Sentinel or Cluster for high availability and failover of queue instances.</li>
                  <li>Always configure TTL and exponential backoff retry strategies for transient network drops.</li>
                </ul>
              </div>
            </div>
          )}

          {/* TAB 2: DAILY QUIZ */}
          {activeTab === 'quiz' && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div>
                  <h3 style={{ fontSize: '1.15rem', fontWeight: 800, margin: '0 0 4px 0' }}>
                    Day {selectedDay} Knowledge Check
                  </h3>
                  <span style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)' }}>
                    Answer 2 or more correctly to advance your daily streak flame 🔥
                  </span>
                </div>
                {quizSubmitted && (
                  <span className={`client-badge ${quizScore >= 2 ? 'client-badge-green' : 'client-badge-orange'}`}>
                    Score: {quizScore} / {quizQuestions.length} ({Math.round((quizScore / quizQuestions.length) * 100)}%)
                  </span>
                )}
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                {quizQuestions.map((q, qIdx) => (
                  <div key={q.id} style={{ background: 'rgba(255, 255, 255, 0.03)', padding: '16px', borderRadius: '12px', border: '1px solid var(--app-card-border)' }}>
                    <div style={{ fontSize: '0.875rem', fontWeight: 700, marginBottom: '12px' }}>
                      {qIdx + 1}. {q.question}
                    </div>

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                      {q.options.map((opt, optIdx) => {
                        const isSelected = quizAnswers[qIdx] === optIdx;
                        const isCorrect = q.correct === optIdx;
                        let bg = 'rgba(255, 255, 255, 0.04)';
                        let borderColor = 'transparent';

                        if (quizSubmitted) {
                          if (isCorrect) {
                            bg = 'rgba(16, 185, 129, 0.15)';
                            borderColor = '#10B981';
                          } else if (isSelected && !isCorrect) {
                            bg = 'rgba(239, 68, 68, 0.15)';
                            borderColor = '#EF4444';
                          }
                        } else if (isSelected) {
                          bg = 'rgba(255, 106, 0, 0.15)';
                          borderColor = 'var(--app-orange)';
                        }

                        return (
                          <button
                            key={optIdx}
                            onClick={() => handleSelectOption(qIdx, optIdx)}
                            style={{
                              display: 'flex',
                              alignItems: 'center',
                              gap: '10px',
                              padding: '10px 14px',
                              borderRadius: '8px',
                              background: bg,
                              border: `1px solid ${borderColor}`,
                              color: '#FFFFFF',
                              cursor: quizSubmitted ? 'default' : 'pointer',
                              textAlign: 'left',
                              fontSize: '0.8125rem',
                              width: '100%',
                            }}
                          >
                            <span style={{ width: '18px', height: '18px', borderRadius: '50%', border: '1.5px solid rgba(255, 255, 255, 0.4)', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '0.6875rem', fontWeight: 700, flexShrink: 0 }}>
                              {String.fromCharCode(65 + optIdx)}
                            </span>
                            <span>{opt}</span>
                          </button>
                        );
                      })}
                    </div>

                    {quizSubmitted && (
                      <div style={{ marginTop: '10px', fontSize: '0.75rem', color: '#94A3B8', padding: '8px 12px', background: 'rgba(0, 0, 0, 0.2)', borderRadius: '6px' }}>
                        💡 <strong>Explanation:</strong> {q.explanation}
                      </div>
                    )}
                  </div>
                ))}
              </div>

              {!quizSubmitted ? (
                <button
                  onClick={submitQuiz}
                  disabled={Object.keys(quizAnswers).length < quizQuestions.length}
                  className="client-btn client-btn-primary"
                  style={{ alignSelf: 'flex-start' }}
                >
                  <Flame size={16} />
                  <span>Submit Answers & Claim Streak</span>
                </button>
              ) : (
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px', background: 'rgba(16, 185, 129, 0.1)', padding: '12px 18px', borderRadius: '10px', border: '1px solid rgba(16, 185, 129, 0.3)' }}>
                  <Flame size={20} color="#FF6A00" />
                  <span style={{ fontSize: '0.875rem', fontWeight: 700, color: '#34D399' }}>
                    +1 Day Streak Active! Keep up the daily learning pace.
                  </span>
                </div>
              )}
            </div>
          )}

          {/* TAB 3: PERSONAL NOTES */}
          {activeTab === 'notes' && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <h3 style={{ fontSize: '1.15rem', fontWeight: 800, margin: 0 }}>
                  Personal Study Notes for Day {selectedDay}
                </h3>
                <button
                  onClick={handleSaveNote}
                  className="client-btn client-btn-primary"
                  style={{ padding: '6px 14px', fontSize: '0.8125rem' }}
                >
                  {noteSaved ? <Check size={14} /> : <Save size={14} />}
                  <span>{noteSaved ? 'Saved to Profile!' : 'Save Notes'}</span>
                </button>
              </div>

              <textarea
                value={noteContent}
                onChange={(e) => setNoteContent(e.target.value)}
                className="client-textarea"
                rows={10}
                placeholder="Write your code snippets, architectural trade-offs, and key takeaways for this day..."
              />
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
