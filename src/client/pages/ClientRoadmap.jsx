import React, { useState } from 'react';
import { 
  PlayCircle, 
  Flame, 
  Bookmark, 
  BookmarkCheck, 
  CheckCircle2, 
  Save, 
  Check,
  Compass,
  Sparkles,
  BookOpen,
  ArrowRight,
  Search
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function ClientRoadmap() {
  const { user, updateUser } = useAuth();
  const [viewMode, setViewMode] = useState('learning'); // 'learning' | 'hub' | 'create'

  // Active Roadmap state
  const [activeRoadmap, setActiveRoadmap] = useState({
    id: 'python-dev',
    title: 'Python Backend & Microservices',
    totalDays: 36,
    completedDays: 13,
    activeDay: 14,
  });

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

  // Catalog State
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('All');
  const [customRoleTitle, setCustomRoleTitle] = useState('');
  const [customDuration, setCustomDuration] = useState('6 Months');

  const catalog = [
    {
      id: 'dsa-cse',
      title: 'Data Structures & Algorithms (DSA)',
      category: 'Core CSE',
      badge: 'Core Foundation',
      description: 'Master arrays, linked lists, trees, graphs, sorting, and dynamic programming with LeetCode patterns.',
      skills: ['Arrays', 'Trees', 'Graphs', 'Dynamic Programming', 'LeetCode'],
      totalDays: 40,
    },
    {
      id: 'python-dev',
      title: 'Python Backend & Microservices',
      category: 'Programming Languages',
      badge: 'High Demand',
      description: 'Python 3 OOP, Asyncio, FastAPI microservices, PostgreSQL, Alembic, and Docker containers.',
      skills: ['Python 3', 'FastAPI', 'PostgreSQL', 'Asyncio', 'Docker'],
      totalDays: 36,
    },
    {
      id: 'android-kotlin',
      title: 'Android Development (Kotlin & Compose)',
      category: 'Mobile Development',
      badge: 'Native Android',
      description: 'Build modern reactive Android apps with Kotlin, Jetpack Compose, Coroutines/Flow, and Retrofit.',
      skills: ['Android', 'Kotlin', 'Jetpack Compose', 'Coroutines', 'Flow', 'Retrofit'],
      totalDays: 30,
    },
    {
      id: 'java-dev',
      title: 'Enterprise Java & Spring Boot',
      category: 'Programming Languages',
      badge: 'Enterprise Core',
      description: 'Core Java, Collections, Multithreading, Spring Boot 3, Hibernate/JPA, and Microservices.',
      skills: ['Java 21', 'Spring Boot', 'JPA/Hibernate', 'Microservices'],
      totalDays: 30,
    },
    {
      id: 'react-fullstack',
      title: 'Full Stack Web (React & Node)',
      category: 'Web & Mobile',
      badge: 'Industry Standard',
      description: 'React 19, TypeScript, Tailwind CSS, Node.js REST APIs, and Cloud Deployment.',
      skills: ['React', 'TypeScript', 'Node.js', 'Tailwind', 'MongoDB'],
      totalDays: 30,
    },
    {
      id: 'ai-ml',
      title: 'AI Engineering & LLMs',
      category: 'AI & Data Science',
      badge: 'Cutting Edge',
      description: 'PyTorch, Hugging Face Transformers, LangChain, Vector Databases, and Agentic Workflows.',
      skills: ['PyTorch', 'Hugging Face', 'LangChain', 'Vector DB', 'RAG'],
      totalDays: 35,
    },
  ];

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
        'It prevents CPU-heavy or blocking I/O tasks from starving the HTTP event loop',
        'It eliminates the need for database migrations',
        'It bypasses JWT token verification',
        'It automatically scales to 100,000 servers without configuration',
      ],
      correct: 0,
      explanation: 'Offloading background tasks to Celery or Redis workers keeps the web server fast and responsive to HTTP requests.',
    },
  ];

  const videoUrls = {
    en: 'https://www.youtube-nocookie.com/embed/0sOVMULO1Ys?rel=0',
    te: 'https://www.youtube-nocookie.com/embed/0sOVMULO1Ys?rel=0',
    hi: 'https://www.youtube-nocookie.com/embed/0sOVMULO1Ys?rel=0',
  };

  const handleSelectAnswer = (qId, optionIdx) => {
    if (quizSubmitted) return;
    setQuizAnswers({ ...quizAnswers, [qId]: optionIdx });
  };

  const handleSubmitQuiz = () => {
    let score = 0;
    quizQuestions.forEach((q) => {
      if (quizAnswers[q.id] === q.correct) {
        score++;
      }
    });
    setQuizScore(score);
    setQuizSubmitted(true);
    if (score === quizQuestions.length) {
      updateUser({ streak: (user?.streak || 3) + 1 });
    }
  };

  const handleSaveNote = () => {
    setNoteSaved(true);
    setTimeout(() => setNoteSaved(false), 2500);
  };

  const handleSwitchCurriculum = (item) => {
    setActiveRoadmap({
      id: item.id,
      title: item.title,
      totalDays: item.totalDays,
      completedDays: 1,
      activeDay: 1,
    });
    setSelectedDay(1);
    setViewMode('learning');
  };

  const filteredCatalog = catalog.filter((item) => {
    const matchesCat = selectedCategory === 'All' || item.category === selectedCategory;
    const matchesSearch = item.title.toLowerCase().includes(searchQuery.toLowerCase()) || item.skills.some((s) => s.toLowerCase().includes(searchQuery.toLowerCase()));
    return matchesCat && matchesSearch;
  });

  return (
    <div style={{ maxWidth: '1180px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '20px' }}>
      {/* Top Header & View Switcher */}
      <div className="client-card" style={{ padding: '20px 28px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '16px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '14px' }}>
          <div style={{ width: '44px', height: '44px', borderRadius: '12px', background: 'linear-gradient(135deg, #FF6A00 0%, #FF8A3D 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF' }}>
            <Compass size={22} />
          </div>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
              <h2 style={{ fontSize: '1.25rem', fontWeight: 800, margin: 0 }}>
                {viewMode === 'learning' ? activeRoadmap.title : 'Curriculum Hub & Catalog'}
              </h2>
              <span className="client-badge client-badge-orange">DAY {selectedDay}</span>
            </div>
            <span style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)' }}>
              Structured AI Learning Path • English, Telugu & Hindi Modules
            </span>
          </div>
        </div>

        <div style={{ display: 'flex', gap: '8px' }}>
          <button
            onClick={() => setViewMode('learning')}
            className={`client-btn ${viewMode === 'learning' ? 'client-btn-primary' : 'client-btn-secondary'}`}
            style={{ padding: '8px 16px', fontSize: '0.8125rem' }}
          >
            <PlayCircle size={15} />
            <span>Daily Learning</span>
          </button>
          <button
            onClick={() => setViewMode('hub')}
            className={`client-btn ${viewMode === 'hub' ? 'client-btn-primary' : 'client-btn-secondary'}`}
            style={{ padding: '8px 16px', fontSize: '0.8125rem' }}
          >
            <BookOpen size={15} />
            <span>Browse Roadmaps</span>
          </button>
          <button
            onClick={() => setViewMode('create')}
            className={`client-btn ${viewMode === 'create' ? 'client-btn-primary' : 'client-btn-secondary'}`}
            style={{ padding: '8px 16px', fontSize: '0.8125rem' }}
          >
            <Sparkles size={15} />
            <span>Generate AI Path</span>
          </button>
        </div>
      </div>

      {/* VIEW 1: DAY LEARNING PLAYER */}
      {viewMode === 'learning' && (
        <div style={{ display: 'grid', gridTemplateColumns: '320px 1fr', gap: '20px', alignItems: 'start' }}>
          {/* Left Column: Syllabus Days Navigator */}
          <div className="client-card" style={{ padding: '20px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
              <h3 style={{ fontSize: '1rem', fontWeight: 800, margin: 0 }}>Course Curriculum</h3>
              <span style={{ fontSize: '0.75rem', color: 'var(--app-orange)', fontWeight: 700 }}>
                {activeRoadmap.completedDays} / {activeRoadmap.totalDays} Days
              </span>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
              {daysList.map((d) => (
                <button
                  key={d.day}
                  onClick={() => {
                    setSelectedDay(d.day);
                    setQuizSubmitted(false);
                    setQuizAnswers({});
                  }}
                  className="client-btn"
                  style={{
                    width: '100%',
                    justifyContent: 'flex-start',
                    padding: '12px 14px',
                    borderRadius: '10px',
                    background: selectedDay === d.day ? 'var(--app-orange-light)' : 'var(--app-surface-light)',
                    border: selectedDay === d.day ? '1.5px solid var(--app-orange)' : '1px solid var(--app-border-subtle)',
                    color: selectedDay === d.day ? 'var(--app-orange)' : 'var(--app-text)',
                    textAlign: 'left',
                  }}
                >
                  <div style={{ display: 'flex', alignItems: 'center', gap: '10px', width: '100%' }}>
                    {d.completed ? (
                      <CheckCircle2 size={16} color="#10B981" />
                    ) : (
                      <span style={{ width: '16px', height: '16px', borderRadius: '50%', border: '2px solid #64748B' }} />
                    )}
                    <div style={{ flex: 1, minWidth: 0 }}>
                      <div style={{ fontSize: '0.6875rem', color: 'var(--app-orange)', fontWeight: 700 }}>DAY {d.day}</div>
                      <div style={{ fontSize: '0.8125rem', fontWeight: 600, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                        {d.title}
                      </div>
                    </div>
                  </div>
                </button>
              ))}
            </div>
          </div>

          {/* Right Column: Player & Day Modules */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
            {/* Action Bar (Language Tabs, Bookmark) */}
            <div className="client-card" style={{ padding: '14px 20px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '12px' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                <span style={{ fontSize: '0.8125rem', fontWeight: 700, color: 'var(--app-text-secondary)' }}>Audio / Video Language:</span>
                <div className="client-tabs" style={{ marginBottom: 0 }}>
                  <button onClick={() => setSelectedLang('en')} className={`client-tab-btn ${selectedLang === 'en' ? 'active' : ''}`} style={{ padding: '4px 12px', fontSize: '0.75rem' }}>English</button>
                  <button onClick={() => setSelectedLang('te')} className={`client-tab-btn ${selectedLang === 'te' ? 'active' : ''}`} style={{ padding: '4px 12px', fontSize: '0.75rem' }}>తెలుగు (Telugu)</button>
                  <button onClick={() => setSelectedLang('hi')} className={`client-tab-btn ${selectedLang === 'hi' ? 'active' : ''}`} style={{ padding: '4px 12px', fontSize: '0.75rem' }}>हिन्दी (Hindi)</button>
                </div>
              </div>

              <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <button
                  onClick={() => setIsBookmarked(!isBookmarked)}
                  className="client-btn client-btn-secondary"
                  style={{ padding: '6px 12px', fontSize: '0.8125rem', color: isBookmarked ? '#F59E0B' : 'inherit' }}
                >
                  {isBookmarked ? <BookmarkCheck size={16} /> : <Bookmark size={16} />}
                  <span>{isBookmarked ? 'Bookmarked' : 'Bookmark'}</span>
                </button>
              </div>
            </div>

            {/* Video Player */}
            <div className="client-card" style={{ padding: '16px', borderRadius: '16px' }}>
              <div style={{ position: 'relative', width: '100%', paddingBottom: '56.25%', height: 0, borderRadius: '12px', overflow: 'hidden', background: '#000000' }}>
                <iframe
                  src={videoUrls[selectedLang]}
                  title="Curriculum Learning Video"
                  style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', border: 0 }}
                  allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                  allowFullScreen
                />
              </div>
            </div>

            {/* Activity Tabs: Video Details, Interactive Quiz, Notes */}
            <div className="client-tabs">
              <button onClick={() => setActiveTab('video')} className={`client-tab-btn ${activeTab === 'video' ? 'active' : ''}`}>
                <PlayCircle size={15} />
                <span>Concept Overview</span>
              </button>
              <button onClick={() => setActiveTab('quiz')} className={`client-tab-btn ${activeTab === 'quiz' ? 'active' : ''}`}>
                <Flame size={15} />
                <span>Daily Quiz (3 Questions)</span>
              </button>
              <button onClick={() => setActiveTab('notes')} className={`client-tab-btn ${activeTab === 'notes' ? 'active' : ''}`}>
                <Save size={15} />
                <span>Personal Notes</span>
              </button>
            </div>

            {/* TAB: OVERVIEW */}
            {activeTab === 'video' && (
              <div className="client-card" style={{ padding: '24px' }}>
                <h3 style={{ fontSize: '1.125rem', fontWeight: 800, margin: '0 0 10px 0' }}>
                  Day {selectedDay}: Asynchronous Workers & Redis Concurrency
                </h3>
                <p style={{ color: 'var(--app-text-secondary)', lineHeight: 1.6, fontSize: '0.875rem', margin: '0 0 16px 0' }}>
                  In high-throughput microservice architectures, requests that require expensive background computing (PDF report generation, email delivery, AI inference) should never block the ASGI event loop. Today we master Redis queue patterns, worker dead-letter queues, and distributed lock TTLs.
                </p>
                <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                  <span className="client-badge client-badge-blue">Redis Queues</span>
                  <span className="client-badge client-badge-blue">Celery Workers</span>
                  <span className="client-badge client-badge-blue">Distributed Locks</span>
                  <span className="client-badge client-badge-blue">Deadlock Prevention</span>
                </div>
              </div>
            )}

            {/* TAB: QUIZ */}
            {activeTab === 'quiz' && (
              <div className="client-card" style={{ padding: '24px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
                  <div>
                    <h3 style={{ fontSize: '1.125rem', fontWeight: 800, margin: '0 0 4px 0' }}>Day {selectedDay} Knowledge Verification</h3>
                    <span style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)' }}>Score 100% to advance your streak 🔥</span>
                  </div>
                  {quizSubmitted && (
                    <span className="client-badge client-badge-orange" style={{ fontSize: '0.875rem', padding: '6px 14px' }}>
                      Score: {quizScore} / {quizQuestions.length}
                    </span>
                  )}
                </div>

                <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                  {quizQuestions.map((q, idx) => (
                    <div key={q.id} style={{ padding: '18px', background: 'var(--app-surface-light)', borderRadius: '12px', border: '1px solid var(--app-border-subtle)' }}>
                      <div style={{ fontWeight: 800, fontSize: '0.9375rem', marginBottom: '12px', color: 'var(--app-text)' }}>
                        {idx + 1}. {q.question}
                      </div>

                      <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                        {q.options.map((opt, optIdx) => {
                          const isSelected = quizAnswers[q.id] === optIdx;
                          const isCorrect = q.correct === optIdx;
                          let bg = '#FFFFFF';
                          let border = '1.5px solid #CBD5E1';
                          let textColor = 'var(--app-text)';

                          if (quizSubmitted) {
                            if (isCorrect) {
                              bg = 'var(--app-success-bg)';
                              border = '1.5px solid #10B981';
                              textColor = '#047857';
                            } else if (isSelected && !isCorrect) {
                              bg = 'var(--app-danger-bg)';
                              border = '1.5px solid #EF4444';
                              textColor = '#DC2626';
                            }
                          } else if (isSelected) {
                            bg = 'var(--app-orange-light)';
                            border = '1.5px solid var(--app-orange)';
                            textColor = 'var(--app-orange)';
                          }

                          return (
                            <button
                              key={optIdx}
                              onClick={() => handleSelectAnswer(q.id, optIdx)}
                              className="client-btn"
                              style={{ width: '100%', justifyContent: 'flex-start', padding: '10px 14px', background: bg, border: border, borderRadius: '8px', fontSize: '0.8125rem', color: textColor, fontWeight: 600 }}
                            >
                              <span>{opt}</span>
                            </button>
                          );
                        })}
                      </div>

                      {quizSubmitted && (
                        <div style={{ marginTop: '12px', fontSize: '0.8125rem', color: '#0369A1', padding: '10px 14px', background: 'var(--app-info-bg)', border: '1px solid rgba(2, 132, 199, 0.25)', borderRadius: '8px' }}>
                          💡 <strong>Explanation</strong>: {q.explanation}
                        </div>
                      )}
                    </div>
                  ))}
                </div>

                <div style={{ marginTop: '20px', display: 'flex', justifyContent: 'flex-end' }}>
                  {!quizSubmitted ? (
                    <button
                      onClick={handleSubmitQuiz}
                      disabled={Object.keys(quizAnswers).length < quizQuestions.length}
                      className="client-btn client-btn-primary"
                      style={{ padding: '10px 24px' }}
                    >
                      <span>Submit Quiz Answers</span>
                    </button>
                  ) : (
                    <button
                      onClick={() => {
                        setQuizSubmitted(false);
                        setQuizAnswers({});
                      }}
                      className="client-btn client-btn-secondary"
                      style={{ padding: '10px 20px' }}
                    >
                      <span>Retry Quiz</span>
                    </button>
                  )}
                </div>
              </div>
            )}

            {/* TAB: NOTES */}
            {activeTab === 'notes' && (
              <div className="client-card" style={{ padding: '24px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '14px' }}>
                  <h3 style={{ fontSize: '1.125rem', fontWeight: 800, margin: 0 }}>Day {selectedDay} Study Notes</h3>
                  <button onClick={handleSaveNote} className="client-btn client-btn-primary" style={{ padding: '8px 16px', fontSize: '0.8125rem' }}>
                    {noteSaved ? <Check size={14} /> : <Save size={14} />}
                    <span>{noteSaved ? 'Saved!' : 'Save Notes'}</span>
                  </button>
                </div>
                <textarea
                  rows={8}
                  value={noteContent}
                  onChange={(e) => setNoteContent(e.target.value)}
                  className="client-input"
                  style={{ width: '100%', resize: 'vertical', lineHeight: 1.6 }}
                  placeholder="Record your code snippets, command flags, and interview takeaways..."
                />
              </div>
            )}
          </div>
        </div>
      )}

      {/* VIEW 2: ROADMAP HUB & CATALOG */}
      {viewMode === 'hub' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          {/* Filter / Search Bar */}
          <div className="client-card" style={{ padding: '20px 24px', display: 'flex', gap: '14px', alignItems: 'center', flexWrap: 'wrap' }}>
            <div style={{ flex: 1, minWidth: '240px', position: 'relative' }}>
              <input
                type="text"
                placeholder="Search curricula (e.g. Python, Android, DSA)..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="client-input"
                style={{ width: '100%', paddingLeft: '36px' }}
              />
              <Search size={16} color="#94A3B8" style={{ position: 'absolute', left: '12px', top: '50%', transform: 'translateY(-50%)' }} />
            </div>

            <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
              {['All', 'Core CSE', 'Programming Languages', 'Mobile Development', 'AI & Data Science'].map((cat) => (
                <button
                  key={cat}
                  onClick={() => setSelectedCategory(cat)}
                  className={`client-btn ${selectedCategory === cat ? 'client-btn-primary' : 'client-btn-secondary'}`}
                  style={{ padding: '6px 14px', fontSize: '0.75rem' }}
                >
                  {cat}
                </button>
              ))}
            </div>
          </div>

          {/* Catalog Grid */}
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: '20px' }}>
            {filteredCatalog.map((item) => (
              <div key={item.id} className="client-card client-card-glow" style={{ padding: '24px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                <div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '10px' }}>
                    <span className="client-badge client-badge-orange">{item.badge}</span>
                    <span style={{ fontSize: '0.75rem', color: '#64748B', fontWeight: 600 }}>{item.totalDays} Days</span>
                  </div>
                  <h3 style={{ fontSize: '1.125rem', fontWeight: 800, margin: '0 0 8px 0' }}>{item.title}</h3>
                  <p style={{ color: 'var(--app-text-secondary)', fontSize: '0.8125rem', lineHeight: 1.5, margin: '0 0 16px 0' }}>
                    {item.description}
                  </p>
                  <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap', marginBottom: '20px' }}>
                    {item.skills.map((s, idx) => (
                      <span key={idx} className="client-badge client-badge-blue" style={{ fontSize: '0.6875rem' }}>{s}</span>
                    ))}
                  </div>
                </div>

                <button
                  onClick={() => handleSwitchCurriculum(item)}
                  className="client-btn client-btn-primary"
                  style={{ width: '100%', padding: '10px' }}
                >
                  <span>Select Curriculum</span>
                  <ArrowRight size={15} />
                </button>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* VIEW 3: AI CUSTOM ROADMAP GENERATOR */}
      {viewMode === 'create' && (
        <div className="client-card client-card-glow" style={{ maxWidth: '640px', margin: '0 auto', padding: '36px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '16px' }}>
            <div style={{ width: '48px', height: '48px', borderRadius: '12px', background: 'linear-gradient(135deg, #FF6A00 0%, #FF8A3D 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF' }}>
              <Sparkles size={24} />
            </div>
            <div>
              <h3 style={{ fontSize: '1.25rem', fontWeight: 800, margin: 0 }}>AI Custom Roadmap Generator</h3>
              <span style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)' }}>Synthesizes personalized day-by-day learning curricula</span>
            </div>
          </div>

          <p style={{ fontSize: '0.875rem', color: 'var(--app-text-secondary)', lineHeight: 1.6, marginBottom: '24px' }}>
            Enter any target tech stack or specialized engineering role. JobPilot AI will construct a 30 to 60-day syllabus with embedded tutorials, quiz milestones, and project goals.
          </p>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px', marginBottom: '24px' }}>
            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                Target Engineering Role or Technology
              </label>
              <input
                type="text"
                placeholder="e.g. Distributed Systems Engineer, Kubernetes Operator, React Native..."
                value={customRoleTitle}
                onChange={(e) => setCustomRoleTitle(e.target.value)}
                className="client-input"
              />
            </div>

            <div>
              <label style={{ display: 'block', fontSize: '0.8125rem', fontWeight: 700, marginBottom: '6px', color: 'var(--app-text-secondary)' }}>
                Target Duration
              </label>
              <div style={{ display: 'flex', gap: '10px' }}>
                {['3 Months (30 Days)', '6 Months (60 Days)', '12 Months (120 Days)'].map((dur) => (
                  <button
                    key={dur}
                    onClick={() => setCustomDuration(dur)}
                    className={`client-btn ${customDuration === dur ? 'client-btn-primary' : 'client-btn-secondary'}`}
                    style={{ flex: 1, padding: '10px', fontSize: '0.75rem', justifyContent: 'center' }}
                  >
                    {dur}
                  </button>
                ))}
              </div>
            </div>
          </div>

          <button
            onClick={() => {
              if (!customRoleTitle.trim()) return;
              handleSwitchCurriculum({
                id: 'custom-' + Date.now(),
                title: `${customRoleTitle} Track`,
                totalDays: 45,
              });
            }}
            disabled={!customRoleTitle.trim()}
            className="client-btn client-btn-primary"
            style={{ width: '100%', padding: '14px' }}
          >
            <Sparkles size={18} />
            <span>Generate & Launch Roadmap</span>
          </button>
        </div>
      )}
    </div>
  );
}
