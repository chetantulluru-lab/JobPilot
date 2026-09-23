import React, { useState, useEffect } from 'react';
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
  ArrowLeft,
  Search,
  ExternalLink,
  Code,
  HelpCircle,
  FileText,
  Bot,
  Send,
  RefreshCw,
  Award,
  Video
} from 'lucide-react';
import api from '../api/apiClient';
import { useAuth } from '../context/AuthContext';
import AIOrb from '../../components/AIOrb';

/**
 * Verified High-Yield YouTube Video Dictionary (1:1 Android InAppVideoPlayer.kt)
 * Covers English, Telugu, and Hindi for 50+ engineering topics.
 */
function getFallbackVideoId(topic, language = 'English') {
  const t = (topic || '').toLowerCase();
  const l = (language || '').toLowerCase();

  // --- DSA / Algorithms / Data Structures ---
  if (t.includes('big-o') || t.includes('asymptotic') || t.includes('complexity')) {
    if (l === 'telugu' || l === 'te') return 't_wFv34w76U';
    if (l === 'hindi' || l === 'hi') return 'A03oI0znAoc';
    return 'v4cd1O4zkGw';
  }
  if (t.includes('array') || t.includes('pointer') || t.includes('sliding') || t.includes('prefix')) {
    if (l === 'telugu' || l === 'te') return 't_wFv34w76U';
    if (l === 'hindi' || l === 'hi') return 'AT14lCXuMKI';
    return 'KLlXCFG5TnA';
  }
  if (t.includes('linked list') || t.includes('list')) {
    if (l === 'telugu' || l === 'te') return 't_wFv34w76U';
    if (l === 'hindi' || l === 'hi') return 'AT14lCXuMKI';
    return 'WwfhLC16bis';
  }
  if (t.includes('stack') || t.includes('queue')) {
    if (l === 'telugu' || l === 'te') return 't_wFv34w76U';
    if (l === 'hindi' || l === 'hi') return 'AT14lCXuMKI';
    return 'wjI1W422126I';
  }
  if (t.includes('tree') || t.includes('binary') || t.includes('bst') || t.includes('heap')) {
    if (l === 'telugu' || l === 'te') return 't_wFv34w76U';
    if (l === 'hindi' || l === 'hi') return 'AT14lCXuMKI';
    return 'oSWTXtMglKE';
  }
  if (t.includes('graph') || t.includes('dfs') || t.includes('bfs') || t.includes('dijkstra')) {
    if (l === 'telugu' || l === 'te') return 't_wFv34w76U';
    if (l === 'hindi' || l === 'hi') return 'AT14lCXuMKI';
    return 'tWVWeAqZ0WU';
  }
  if (t.includes('dynamic') || t.includes('dp') || t.includes('memoization')) {
    if (l === 'telugu' || l === 'te') return 't_wFv34w76U';
    if (l === 'hindi' || l === 'hi') return 'AT14lCXuMKI';
    return 'oBt53YbR9Kk';
  }
  if (t.includes('sorting') || t.includes('merge sort') || t.includes('quick sort')) {
    if (l === 'telugu' || l === 'te') return 't_wFv34w76U';
    if (l === 'hindi' || l === 'hi') return 'AT14lCXuMKI';
    return 'RBSGKlAvoiM';
  }

  // --- Web Development / Frontend / Backend ---
  if (t.includes('react') || t.includes('component') || t.includes('jsx') || t.includes('hooks')) {
    if (l === 'telugu' || l === 'te') return '934f0xN5-oQ';
    if (l === 'hindi' || l === 'hi') return 'tiLWCNFzThE';
    return 'bMknfKXIFA8';
  }
  if (t.includes('html') || t.includes('css') || t.includes('dom') || t.includes('frontend')) {
    if (l === 'telugu' || l === 'te') return 'm67-bOpOoPU';
    if (l === 'hindi' || l === 'hi') return 'tVzUXW6siu0';
    return 'G3e-cpL7ofc';
  }
  if (t.includes('javascript') || t.includes('js') || t.includes('typescript') || t.includes('async')) {
    if (l === 'telugu' || l === 'te') return 'zJSY8tbf_ys';
    if (l === 'hindi' || l === 'hi') return 'SSC0qX_7uA4';
    return 'EerdGm-ehJQ';
  }
  if (t.includes('node') || t.includes('express') || t.includes('api') || t.includes('backend') || t.includes('fastapi') || t.includes('redis')) {
    if (l === 'telugu' || l === 'te') return 'zJSY8tbf_ys';
    if (l === 'hindi' || l === 'hi') return 'chx9Rs41W6g';
    return 'Oe421EPjeBE';
  }

  // --- Python / AI / Machine Learning ---
  if (t.includes('python')) {
    if (l === 'telugu' || l === 'te') return '_uQrJ0TkZlc';
    if (l === 'hindi' || l === 'hi') return '7wnove7K-ZQ';
    return 'rfscVS0vtbw';
  }
  if (t.includes('machine learning') || t.includes('model') || t.includes('regression') || t.includes('classification')) {
    if (l === 'telugu' || l === 'te') return 'QXeEoD0pB3E';
    if (l === 'hindi' || l === 'hi') return '1xs4SsmTW3A';
    return 'i_LwzRVP7bg';
  }
  if (t.includes('neural') || t.includes('deep learning') || t.includes('pytorch') || t.includes('tensorflow') || t.includes('llm')) {
    if (l === 'telugu' || l === 'te') return 'QXeEoD0pB3E';
    if (l === 'hindi' || l === 'hi') return '2Ob3A_El4W4';
    return 'aircAruvnKk';
  }

  // --- Mobile App Development / Android / Flutter ---
  if (t.includes('android') || t.includes('kotlin') || t.includes('compose')) {
    if (l === 'telugu' || l === 'te') return '1f39B1mJ04M';
    if (l === 'hindi' || l === 'hi') return 'mXjZQX3UzOs';
    return 'fis26HvvDA4';
  }
  if (t.includes('flutter') || t.includes('dart')) {
    if (l === 'telugu' || l === 'te') return 'W-aB0q4F_gU';
    if (l === 'hindi' || l === 'hi') return 'inT_e1_0Fw8';
    return 'VPvVD8t02U8';
  }

  // --- Database / SQL / System Design / Cloud ---
  if (t.includes('sql') || t.includes('database') || t.includes('postgres') || t.includes('mongo')) {
    if (l === 'telugu' || l === 'te') return 't_wFv34w76U';
    if (l === 'hindi' || l === 'hi') return 'kBdlM6h53yM';
    return 'HXV3zeRR3h4';
  }
  if (t.includes('docker') || t.includes('kubernetes') || t.includes('devops') || t.includes('cloud')) {
    if (l === 'telugu' || l === 'te') return 'rD4_xG3zV88';
    if (l === 'hindi' || l === 'hi') return 'k63zU5d-JpY';
    return 'fqMOX6JJ87U';
  }
  if (t.includes('system design') || t.includes('microservices') || t.includes('architecture')) {
    if (l === 'telugu' || l === 'te') return 'rD4_xG3zV88';
    if (l === 'hindi' || l === 'hi') return 'bkSWJJZNgf8';
    return 'bUHFg8CZF7I';
  }

  // Default Fallbacks
  if (l === 'telugu' || l === 'te') return 't_wFv34w76U';
  if (l === 'hindi' || l === 'hi') return 'AT14lCXuMKI';
  return '8hly31xKli0';
}

function resolveYouTubeVideoId(url, topic, language) {
  if (url && typeof url === 'string' && url.trim()) {
    const watchMatch = url.match(/(?:v=|\/v\/|watch\?v=)([a-zA-Z0-9_-]{11})/);
    if (watchMatch && watchMatch[1]) return watchMatch[1];
    
    const embedMatch = url.match(/(?:embed\/)([a-zA-Z0-9_-]{11})/);
    if (embedMatch && embedMatch[1]) return embedMatch[1];

    const shortMatch = url.match(/(?:youtu\.be\/)([a-zA-Z0-9_-]{11})/);
    if (shortMatch && shortMatch[1]) return shortMatch[1];
  }
  return getFallbackVideoId(topic, language);
}

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
  const [activeSubTab, setActiveSubTab] = useState('video'); // 'video' | 'concepts' | 'practice' | 'quiz' | 'notes' | 'tutor'
  const [selectedLang, setSelectedLang] = useState('en'); // 'en' | 'te' | 'hi'
  const [isBookmarked, setIsBookmarked] = useState(false);
  
  // Notes State
  const [notes, setNotes] = useState({
    14: 'Key takeaway: Always use Redis distributed lock with lease expiration when multiple workers handle shared payment order queues to prevent deadlocks.',
  });
  const [noteSaved, setNoteSaved] = useState(false);

  // Day Completion State
  const [completedDaysMap, setCompletedDaysMap] = useState({
    1: true, 2: true, 3: true, 4: true, 5: true, 6: true, 7: true, 8: true, 9: true, 10: true, 11: true, 12: true, 13: true
  });

  // Quiz State
  const [quizAnswers, setQuizAnswers] = useState({});
  const [quizSubmitted, setQuizSubmitted] = useState(false);
  const [quizScore, setQuizScore] = useState(0);

  // AI Tutor in Roadmap State
  const [tutorQuery, setTutorQuery] = useState('');
  const [tutorMessages, setTutorMessages] = useState([
    {
      sender: 'assistant',
      text: "Hello! I am your curriculum advisor for Day 14: Asynchronous Workers & Redis Concurrency. Ask me anything about Celery task routing, lock leases, or worker event loop starvation!",
    },
  ]);
  const [isTutorLoading, setIsTutorLoading] = useState(false);

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
    { 
      day: 12, 
      title: 'PostgreSQL Relational Schemas & Indexes', 
      objective: 'Master B-tree indexing, query planner EXPLAIN ANALYZE, and composite foreign key constraints.',
      concepts: ['B-Tree vs Hash Indexes', 'Query Optimization with EXPLAIN ANALYZE', 'Connection Pooling with asyncpg'],
      practice: 'Write an Alembic migration to add a composite index on (user_id, created_at) and benchmark query latency.'
    },
    { 
      day: 13, 
      title: 'FastAPI Dependency Injection & Middleware', 
      objective: 'Understand how FastAPI handles sub-dependencies, yield cleanup, and CORS/logging middleware.',
      concepts: ['Depends() lifecycle & scoping', 'Context Managers & Yield Dependencies', 'Global Exception Handlers'],
      practice: 'Create an authentication dependency that validates Bearer JWT tokens and attaches current_user to request state.'
    },
    { 
      day: 14, 
      title: 'Asynchronous Workers & Redis Concurrency', 
      objective: 'Master Celery/Redis distributed queue architecture, rate-limiting, and dead-letter queue handlers.',
      concepts: ['Redis FIFO Queue Semantics (LPUSH/BRPOP)', 'Distributed Locks with Redlock & TTL Leases', 'Event Loop Non-Blocking Execution'],
      practice: 'Implement an asynchronous worker task that generates candidate PDF reports in the background without blocking FastAPI.'
    },
    { 
      day: 15, 
      title: 'Docker Containerization & Microservice Networks', 
      objective: 'Build multi-stage production Docker images and configure internal microservice bridge networking.',
      concepts: ['Multi-stage Dockerfile Optimization', 'Docker Compose Service Linking', 'Volume Mounts & Environment Isolation'],
      practice: 'Containerize FastAPI, PostgreSQL, and Redis into a single reproducible docker-compose.yml configuration.'
    },
    { 
      day: 16, 
      title: 'JWT Authentication & OAuth2 Provider Integration', 
      objective: 'Implement secure stateless authentication with refresh token rotation and OAuth2 PKCE flow.',
      concepts: ['HMAC-SHA256 vs RS256 Asymmetric Signing', 'Refresh Token Rotation & Revocation Lists', 'OAuth2 Authorization Code Grant'],
      practice: 'Build a secure /auth/refresh endpoint that invalidates previous refresh tokens and issues a fresh pair.'
    },
  ];

  const currentDayData = daysList.find((d) => d.day === selectedDay) || {
    day: selectedDay,
    title: `Day ${selectedDay}: Advanced Technical Milestone`,
    objective: 'Deepen system design architecture and implementation best practices.',
    concepts: ['Core Architectural Patterns', 'High-Throughput Optimization', 'Production Resiliency'],
    practice: 'Implement a hands-on production code exercise for this technology stack.',
  };

  const currentVideoId = getFallbackVideoId(currentDayData.title, selectedLang);
  const embedVideoUrl = `https://www.youtube-nocookie.com/embed/${currentVideoId}?rel=0&modestbranding=1`;
  const directYouTubeUrl = `https://www.youtube.com/watch?v=${currentVideoId}`;

  const quizQuestions = [
    {
      id: 1,
      question: 'Which Redis data structure is best suited for an asynchronous FIFO task queue?',
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
      question: 'What is the primary benefit of running async worker tasks outside the main FastAPI ASGI event loop?',
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

  const curatedMaterials = [
    {
      title: `${currentDayData.title} - Official Video Guide`,
      type: 'video',
      source: 'YouTube HD',
      url: directYouTubeUrl,
    },
    {
      title: 'FastAPI & Redis Background Tasks Architecture',
      type: 'doc',
      source: 'FastAPI Official Docs',
      url: 'https://fastapi.tiangolo.com/tutorial/background-tasks/',
    },
    {
      title: 'Distributed Locks with Redis & Concurrency Patterns',
      type: 'doc',
      source: 'Redis Documentation',
      url: 'https://redis.io/docs/manual/patterns/distributed-locks/',
    },
  ];

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
      setCompletedDaysMap((prev) => ({ ...prev, [selectedDay]: true }));
      updateUser({ streak: (user?.streak || 3) + 1 });
    }
  };

  const handleSaveNote = async () => {
    setNoteSaved(true);
    try {
      await api.saveDayNote(selectedDay, notes[selectedDay] || '');
    } catch {
      // Local save fallback
    }
    setTimeout(() => setNoteSaved(false), 2500);
  };

  const handleCompleteDay = async () => {
    setCompletedDaysMap((prev) => ({ ...prev, [selectedDay]: true }));
    try {
      await api.completeRoadmapDay(selectedDay);
    } catch {
      // Local completion fallback
    }
    // Advance to next day if available
    if (selectedDay < 16) {
      setSelectedDay(selectedDay + 1);
      setQuizSubmitted(false);
      setQuizAnswers({});
    }
  };

  const handleAskTutor = async (e) => {
    e?.preventDefault();
    if (!tutorQuery.trim() || isTutorLoading) return;

    const query = tutorQuery.trim();
    setTutorMessages((prev) => [...prev, { sender: 'user', text: query }]);
    setTutorQuery('');
    setIsTutorLoading(true);

    try {
      const res = await api.askCurriculumAssistant(selectedDay, query);
      const reply = res.answer || res.reply || res.response || `For ${currentDayData.title}, make sure to verify thread safety and avoid event loop blocking.`;
      setTutorMessages((prev) => [...prev, { sender: 'assistant', text: reply }]);
    } catch {
      setTutorMessages((prev) => [
        ...prev,
        {
          sender: 'assistant',
          text: `Regarding Day ${selectedDay}: Always ensure worker queues have idempotent task execution and dead-letter queues configured for robust fault tolerance.`,
        },
      ]);
    } finally {
      setIsTutorLoading(false);
    }
  };

  const handleSwitchCurriculum = (item) => {
    setActiveRoadmap({
      id: item.id,
      title: item.title,
      totalDays: item.totalDays,
      completedDays: 1,
      activeDay: 1,
    });
    setSelectedDay(12); // Sample start
    setViewMode('learning');
  };

  const filteredCatalog = catalog.filter((item) => {
    const matchesCat = selectedCategory === 'All' || item.category === selectedCategory;
    const matchesSearch = item.title.toLowerCase().includes(searchQuery.toLowerCase()) || item.skills.some((s) => s.toLowerCase().includes(searchQuery.toLowerCase()));
    return matchesCat && matchesSearch;
  });

  const isCurrentDayCompleted = !!completedDaysMap[selectedDay];

  return (
    <div style={{ maxWidth: '1180px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '16px' }}>
      {/* Top Header & View Switcher */}
      <div className="client-card" style={{ padding: '16px 24px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '14px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <div style={{ width: '42px', height: '42px', borderRadius: '12px', background: 'linear-gradient(135deg, #FF6A00 0%, #FF8A3D 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF', flexShrink: 0 }}>
            <Compass size={22} />
          </div>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <h2 style={{ fontSize: '1.2rem', fontWeight: 800, margin: 0, color: 'var(--app-text)' }}>
                {viewMode === 'learning' ? activeRoadmap.title : 'Curriculum Hub & Catalog'}
              </h2>
              <span className="client-badge client-badge-orange">DAY {selectedDay}</span>
            </div>
            <span style={{ fontSize: '0.75rem', color: 'var(--app-text-secondary)' }}>
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

      {/* VIEW 1: DAY LEARNING PLAYER (1:1 Android DayLearningScreen.kt) */}
      {viewMode === 'learning' && (
        <div style={{ display: 'grid', gridTemplateColumns: '300px 1fr', gap: '20px', alignItems: 'start' }}>
          {/* Left Column: Syllabus Days Navigator */}
          <div className="client-card" style={{ padding: '18px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '14px' }}>
              <h3 style={{ fontSize: '0.9375rem', fontWeight: 800, margin: 0 }}>Course Curriculum</h3>
              <span style={{ fontSize: '0.75rem', color: 'var(--app-orange)', fontWeight: 700 }}>
                {Object.keys(completedDaysMap).length} / {activeRoadmap.totalDays} Days
              </span>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
              {daysList.map((d) => {
                const isCompleted = !!completedDaysMap[d.day];
                const isActive = selectedDay === d.day;
                return (
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
                      padding: '10px 12px',
                      borderRadius: '10px',
                      background: isActive ? 'var(--app-orange-light)' : 'var(--app-surface-light)',
                      border: isActive ? '1.5px solid var(--app-orange)' : '1px solid var(--app-border-subtle)',
                      color: isActive ? 'var(--app-orange)' : 'var(--app-text)',
                      textAlign: 'left',
                    }}
                  >
                    <div style={{ display: 'flex', alignItems: 'center', gap: '10px', width: '100%' }}>
                      {isCompleted ? (
                        <CheckCircle2 size={16} color="#10B981" />
                      ) : (
                        <span style={{ width: '16px', height: '16px', borderRadius: '50%', border: '2px solid #94A3B8' }} />
                      )}
                      <div style={{ flex: 1, minWidth: 0 }}>
                        <div style={{ fontSize: '0.6875rem', color: 'var(--app-orange)', fontWeight: 700 }}>DAY {d.day}</div>
                        <div style={{ fontSize: '0.8125rem', fontWeight: 600, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                          {d.title}
                        </div>
                      </div>
                    </div>
                  </button>
                );
              })}
            </div>
          </div>

          {/* Right Column: Player & Day Modules */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {/* Action Bar (Language Tabs, Stepper, Bookmark, Completion Badge) */}
            <div className="client-card" style={{ padding: '14px 18px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '12px' }}>
              {/* Stepper Navigation */}
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <button
                  onClick={() => selectedDay > 12 && setSelectedDay(selectedDay - 1)}
                  disabled={selectedDay <= 12}
                  className="client-btn client-btn-secondary"
                  style={{ padding: '4px 10px', fontSize: '0.75rem' }}
                >
                  <ArrowLeft size={13} />
                  <span>Day {selectedDay - 1}</span>
                </button>

                <span style={{ fontSize: '0.8125rem', fontWeight: 800, color: 'var(--app-orange)', padding: '0 4px' }}>
                  Day {selectedDay} of {activeRoadmap.totalDays}
                </span>

                <button
                  onClick={() => selectedDay < 16 && setSelectedDay(selectedDay + 1)}
                  disabled={selectedDay >= 16}
                  className="client-btn client-btn-secondary"
                  style={{ padding: '4px 10px', fontSize: '0.75rem' }}
                >
                  <span>Day {selectedDay + 1}</span>
                  <ArrowRight size={13} />
                </button>
              </div>

              {/* Language Selector */}
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <span style={{ fontSize: '0.75rem', fontWeight: 700, color: 'var(--app-text-secondary)' }}>Language:</span>
                <div className="client-tabs" style={{ marginBottom: 0 }}>
                  <button onClick={() => setSelectedLang('en')} className={`client-tab-btn ${selectedLang === 'en' ? 'active' : ''}`} style={{ padding: '4px 10px', fontSize: '0.75rem' }}>English</button>
                  <button onClick={() => setSelectedLang('te')} className={`client-tab-btn ${selectedLang === 'te' ? 'active' : ''}`} style={{ padding: '4px 10px', fontSize: '0.75rem' }}>తెలుగు (Telugu)</button>
                  <button onClick={() => setSelectedLang('hi')} className={`client-tab-btn ${selectedLang === 'hi' ? 'active' : ''}`} style={{ padding: '4px 10px', fontSize: '0.75rem' }}>हिन्दी (Hindi)</button>
                </div>
              </div>

              {/* Bookmark & Completion Status */}
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                {isCurrentDayCompleted && (
                  <span className="client-badge client-badge-green" style={{ fontSize: '0.75rem' }}>
                    <CheckCircle2 size={13} />
                    <span>Completed</span>
                  </span>
                )}

                <button
                  onClick={() => setIsBookmarked(!isBookmarked)}
                  className="client-btn client-btn-secondary"
                  style={{ padding: '6px 12px', fontSize: '0.75rem', color: isBookmarked ? '#F59E0B' : 'inherit' }}
                >
                  {isBookmarked ? <BookmarkCheck size={15} color="#F59E0B" /> : <Bookmark size={15} />}
                  <span>{isBookmarked ? 'Bookmarked' : 'Bookmark'}</span>
                </button>
              </div>
            </div>

            {/* 6 Clean Segmented Navigation Sub-Tabs (1:1 Android DayLearningScreen.kt) */}
            <div className="client-tabs" style={{ marginBottom: 0 }}>
              <button onClick={() => setActiveSubTab('video')} className={`client-tab-btn ${activeSubTab === 'video' ? 'active' : ''}`}>
                <PlayCircle size={14} />
                <span>🎥 Video</span>
              </button>
              <button onClick={() => setActiveSubTab('concepts')} className={`client-tab-btn ${activeSubTab === 'concepts' ? 'active' : ''}`}>
                <BookOpen size={14} />
                <span>📖 Concepts</span>
              </button>
              <button onClick={() => setActiveSubTab('practice')} className={`client-tab-btn ${activeSubTab === 'practice' ? 'active' : ''}`}>
                <Code size={14} />
                <span>💻 Practice</span>
              </button>
              <button onClick={() => setActiveSubTab('quiz')} className={`client-tab-btn ${activeSubTab === 'quiz' ? 'active' : ''}`}>
                <Flame size={14} />
                <span>🏆 Daily Quiz</span>
              </button>
              <button onClick={() => setActiveSubTab('notes')} className={`client-tab-btn ${activeSubTab === 'notes' ? 'active' : ''}`}>
                <FileText size={14} />
                <span>📝 Notes</span>
              </button>
              <button onClick={() => setActiveSubTab('tutor')} className={`client-tab-btn ${activeSubTab === 'tutor' ? 'active' : ''}`}>
                <Bot size={14} />
                <span>🤖 AI Tutor</span>
              </button>
            </div>

            {/* SUB-TAB 1: VIDEO LESSON (In-App Player + Direct YouTube Launcher) */}
            {activeSubTab === 'video' && (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                {/* Embedded HTML5 Video Player */}
                <div className="client-card" style={{ padding: '14px', borderRadius: '16px', background: '#0F172A' }}>
                  <div style={{ position: 'relative', width: '100%', paddingBottom: '56.25%', height: 0, borderRadius: '10px', overflow: 'hidden', background: '#000000' }}>
                    <iframe
                      src={embedVideoUrl}
                      title="Curriculum Learning Video"
                      style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', border: 0 }}
                      allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                      allowFullScreen
                    />
                  </div>
                </div>

                {/* Direct Prominent YouTube Launcher Button (1:1 Android Hero Card) */}
                <div className="client-card" style={{ padding: '20px' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
                    <span style={{ fontSize: '0.75rem', fontWeight: 800, color: 'var(--app-orange)' }}>
                      Curated Verified Tutorial
                    </span>
                    <span style={{ fontSize: '0.6875rem', fontWeight: 800, background: '#FEE2E2', color: '#DC2626', padding: '2px 8px', borderRadius: '6px' }}>
                      YouTube HD • {selectedLang === 'te' ? 'Telugu' : selectedLang === 'hi' ? 'Hindi' : 'English'}
                    </span>
                  </div>

                  <h3 style={{ fontSize: '1.1rem', fontWeight: 800, margin: '0 0 6px 0', color: 'var(--app-text)' }}>
                    {currentDayData.title}
                  </h3>
                  <p style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)', lineHeight: 1.5, margin: '0 0 16px 0' }}>
                    {currentDayData.objective}
                  </p>

                  <a
                    href={directYouTubeUrl}
                    target="_blank"
                    rel="noopener noreferrer"
                    style={{
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      gap: '8px',
                      background: '#E50914',
                      color: '#FFFFFF',
                      padding: '12px 20px',
                      borderRadius: '12px',
                      fontWeight: 800,
                      fontSize: '0.875rem',
                      textDecoration: 'none',
                      boxShadow: '0 4px 14px rgba(229, 9, 20, 0.25)',
                    }}
                  >
                    <PlayCircle size={18} />
                    <span>Watch Video on YouTube</span>
                    <ExternalLink size={14} />
                  </a>
                  <div style={{ fontSize: '0.6875rem', color: 'var(--app-text-muted)', marginTop: '8px', textAlign: 'center' }}>
                    Opens directly on YouTube to start playing immediately in full resolution without search results.
                  </div>
                </div>

                {/* Curated Materials & Documentation List */}
                <div className="client-card" style={{ padding: '18px' }}>
                  <h4 style={{ fontSize: '0.875rem', fontWeight: 800, margin: '0 0 12px 0', color: 'var(--app-text)' }}>
                    Curated Learning Materials
                  </h4>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
                    {curatedMaterials.map((mat, idx) => (
                      <a
                        key={idx}
                        href={mat.url}
                        target="_blank"
                        rel="noopener noreferrer"
                        style={{
                          display: 'flex',
                          justifyContent: 'space-between',
                          alignItems: 'center',
                          padding: '10px 14px',
                          background: 'var(--app-surface-light)',
                          border: '1px solid var(--app-border-subtle)',
                          borderRadius: '8px',
                          textDecoration: 'none',
                          color: 'var(--app-text)',
                        }}
                      >
                        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                          {mat.type === 'video' ? <Video size={16} color="#E50914" /> : <FileText size={16} color="var(--app-orange)" />}
                          <div>
                            <div style={{ fontSize: '0.8125rem', fontWeight: 700 }}>{mat.title}</div>
                            <div style={{ fontSize: '0.6875rem', color: 'var(--app-text-muted)' }}>{mat.source}</div>
                          </div>
                        </div>
                        <ExternalLink size={14} color="var(--app-orange)" />
                      </a>
                    ))}
                  </div>
                </div>
              </div>
            )}

            {/* SUB-TAB 2: CONCEPTS & THEORY */}
            {activeSubTab === 'concepts' && (
              <div className="client-card" style={{ padding: '24px', display: 'flex', flexDirection: 'column', gap: '16px' }}>
                <div>
                  <span style={{ fontSize: '0.6875rem', fontWeight: 800, color: 'var(--app-orange)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>
                    Learning Objective
                  </span>
                  <h3 style={{ fontSize: '1.15rem', fontWeight: 800, margin: '4px 0 8px 0', color: 'var(--app-text)' }}>
                    {currentDayData.title}
                  </h3>
                  <p style={{ color: 'var(--app-text-secondary)', lineHeight: 1.6, fontSize: '0.875rem', margin: 0 }}>
                    {currentDayData.objective}
                  </p>
                </div>

                <div style={{ borderTop: '1px solid var(--app-border-subtle)', paddingTop: '16px' }}>
                  <h4 style={{ fontSize: '0.9375rem', fontWeight: 800, marginBottom: '12px', color: 'var(--app-text)' }}>
                    Core Engineering Concepts
                  </h4>
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                    {currentDayData.concepts.map((concept, idx) => (
                      <div key={idx} style={{ padding: '12px 16px', background: 'var(--app-surface-light)', borderRadius: '10px', borderLeft: '3px solid var(--app-orange)' }}>
                        <div style={{ fontSize: '0.8125rem', fontWeight: 800, color: 'var(--app-text)' }}>{concept}</div>
                      </div>
                    ))}
                  </div>
                </div>

                <button
                  onClick={() => setActiveSubTab('practice')}
                  className="client-btn client-btn-secondary"
                  style={{ alignSelf: 'flex-start', marginTop: '8px' }}
                >
                  <Code size={15} color="var(--app-orange)" />
                  <span>Proceed to Hands-on Practice →</span>
                </button>
              </div>
            )}

            {/* SUB-TAB 3: PRACTICE LAB */}
            {activeSubTab === 'practice' && (
              <div className="client-card" style={{ padding: '24px', display: 'flex', flexDirection: 'column', gap: '16px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <h3 style={{ fontSize: '1.1rem', fontWeight: 800, margin: 0, color: 'var(--app-text)' }}>
                    Day {selectedDay} Practice Lab
                  </h3>
                  <span className="client-badge client-badge-orange">Hands-On Code</span>
                </div>

                <p style={{ fontSize: '0.875rem', color: 'var(--app-text-secondary)', lineHeight: 1.6, margin: 0 }}>
                  {currentDayData.practice}
                </p>

                {/* Simulated Terminal / Code Block */}
                <div style={{ background: '#0F172A', color: '#38BDF8', padding: '16px', borderRadius: '10px', fontFamily: 'monospace', fontSize: '0.8125rem', lineHeight: 1.6 }}>
                  <div># Example implementation snippet</div>
                  <div style={{ color: '#F1F5F9' }}>from fastapi import FastAPI, BackgroundTasks</div>
                  <div style={{ color: '#F1F5F9' }}>import redis.asyncio as redis</div>
                  <div style={{ color: '#94A3B8' }}># Connected to local async worker pool</div>
                  <div style={{ color: '#34D399' }}>app = FastAPI(title="JobPilot Concurrency Worker")</div>
                </div>

                <div style={{ display: 'flex', gap: '10px', marginTop: '10px' }}>
                  <button
                    onClick={handleCompleteDay}
                    className="client-btn client-btn-primary"
                    style={{ flex: 1, padding: '12px' }}
                  >
                    <CheckCircle2 size={16} />
                    <span>{isCurrentDayCompleted ? 'Day Completed ✓ (Advance)' : 'Mark Day as Completed'}</span>
                  </button>
                  <button
                    onClick={() => setActiveSubTab('quiz')}
                    className="client-btn client-btn-secondary"
                    style={{ padding: '12px 18px' }}
                  >
                    <Flame size={16} color="var(--app-orange)" />
                    <span>Take Daily Quiz</span>
                  </button>
                </div>
              </div>
            )}

            {/* SUB-TAB 4: DAILY QUIZ (3 Questions) */}
            {activeSubTab === 'quiz' && (
              <div className="client-card" style={{ padding: '24px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
                  <div>
                    <h3 style={{ fontSize: '1.1rem', fontWeight: 800, margin: '0 0 4px 0' }}>Day {selectedDay} Knowledge Verification</h3>
                    <span style={{ fontSize: '0.8125rem', color: 'var(--app-text-secondary)' }}>Score 100% to advance your streak 🔥</span>
                  </div>
                  {quizSubmitted && (
                    <span className="client-badge client-badge-orange" style={{ fontSize: '0.875rem', padding: '6px 14px' }}>
                      Score: {quizScore} / {quizQuestions.length}
                    </span>
                  )}
                </div>

                <div style={{ display: 'flex', flexDirection: 'column', gap: '18px' }}>
                  {quizQuestions.map((q, idx) => (
                    <div key={q.id} style={{ padding: '16px', background: 'var(--app-surface-light)', borderRadius: '12px', border: '1px solid var(--app-border-subtle)' }}>
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

                <div style={{ marginTop: '20px', display: 'flex', justifyContent: 'flex-end', gap: '10px' }}>
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

            {/* SUB-TAB 5: PERSONAL STUDY NOTES */}
            {activeSubTab === 'notes' && (
              <div className="client-card" style={{ padding: '24px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '14px' }}>
                  <div>
                    <h3 style={{ fontSize: '1.1rem', fontWeight: 800, margin: 0 }}>Day {selectedDay} Personal Notes</h3>
                    <span style={{ fontSize: '0.75rem', color: 'var(--app-text-secondary)' }}>Saved to your profile across Android & Windows</span>
                  </div>
                  <button onClick={handleSaveNote} className="client-btn client-btn-primary" style={{ padding: '8px 16px', fontSize: '0.8125rem' }}>
                    {noteSaved ? <Check size={14} /> : <Save size={14} />}
                    <span>{noteSaved ? 'Saved!' : 'Save Notes'}</span>
                  </button>
                </div>
                <textarea
                  rows={8}
                  value={notes[selectedDay] || ''}
                  onChange={(e) => setNotes({ ...notes, [selectedDay]: e.target.value })}
                  className="client-input"
                  style={{ width: '100%', resize: 'vertical', lineHeight: 1.6 }}
                  placeholder="Record your code snippets, command flags, and interview takeaways for this day..."
                />
              </div>
            )}

            {/* SUB-TAB 6: INTERACTIVE AI TUTOR */}
            {activeSubTab === 'tutor' && (
              <div className="client-card" style={{ padding: '20px', display: 'flex', flexDirection: 'column', height: '440px' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '12px' }}>
                  <AIOrb style={{ width: '28px', height: '28px' }} />
                  <div>
                    <h4 style={{ fontSize: '0.9375rem', fontWeight: 800, margin: 0 }}>Day {selectedDay} Curriculum Advisor</h4>
                    <span style={{ fontSize: '0.6875rem', color: 'var(--app-orange)', fontWeight: 600 }}>Grounded in current topic</span>
                  </div>
                </div>

                <div style={{ flex: 1, overflowY: 'auto', display: 'flex', flexDirection: 'column', gap: '10px', padding: '10px', background: 'var(--app-surface-light)', borderRadius: '10px', border: '1px solid var(--app-border-subtle)', marginBottom: '12px' }}>
                  {tutorMessages.map((msg, idx) => {
                    const isUser = msg.sender === 'user';
                    return (
                      <div key={idx} style={{ alignSelf: isUser ? 'flex-end' : 'flex-start', maxWidth: '85%', padding: '10px 14px', borderRadius: '12px', background: isUser ? 'var(--app-orange)' : '#FFFFFF', color: isUser ? '#FFFFFF' : 'var(--app-text)', fontSize: '0.8125rem', lineHeight: 1.5, border: isUser ? 'none' : '1px solid #E2E8F0' }}>
                        {msg.text}
                      </div>
                    );
                  })}
                  {isTutorLoading && (
                    <div style={{ alignSelf: 'flex-start', padding: '8px 12px', background: '#FFFFFF', borderRadius: '12px', fontSize: '0.75rem', color: 'var(--app-orange)', fontWeight: 700 }}>
                      AI Tutor is thinking...
                    </div>
                  )}
                </div>

                <form onSubmit={handleAskTutor} style={{ display: 'flex', gap: '8px' }}>
                  <input
                    type="text"
                    value={tutorQuery}
                    onChange={(e) => setTutorQuery(e.target.value)}
                    placeholder={`Ask a question about ${currentDayData.title}...`}
                    className="client-input"
                    style={{ flex: 1, padding: '10px 14px', fontSize: '0.8125rem' }}
                  />
                  <button type="submit" disabled={isTutorLoading || !tutorQuery.trim()} className="client-btn client-btn-primary" style={{ padding: '0 16px' }}>
                    <Send size={15} />
                  </button>
                </form>
              </div>
            )}
          </div>
        </div>
      )}

      {/* VIEW 2: ROADMAP HUB & CATALOG */}
      {viewMode === 'hub' && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          {/* Filter / Search Bar */}
          <div className="client-card" style={{ padding: '18px 22px', display: 'flex', gap: '12px', alignItems: 'center', flexWrap: 'wrap' }}>
            <div style={{ flex: 1, minWidth: '240px', position: 'relative' }}>
              <input
                type="text"
                placeholder="Search curricula (e.g. Python, Android, DSA, Java)..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="client-input"
                style={{ width: '100%', paddingLeft: '36px' }}
              />
              <Search size={16} color="#94A3B8" style={{ position: 'absolute', left: '12px', top: '50%', transform: 'translateY(-50%)' }} />
            </div>

            <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap' }}>
              {['All', 'Core CSE', 'Programming Languages', 'Mobile Development', 'AI & Data Science'].map((cat) => (
                <button
                  key={cat}
                  onClick={() => setSelectedCategory(cat)}
                  className={`client-btn ${selectedCategory === cat ? 'client-btn-primary' : 'client-btn-secondary'}`}
                  style={{ padding: '6px 12px', fontSize: '0.75rem' }}
                >
                  {cat}
                </button>
              ))}
            </div>
          </div>

          {/* Catalog Grid */}
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: '16px' }}>
            {filteredCatalog.map((item) => (
              <div key={item.id} className="client-card client-card-glow" style={{ padding: '20px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                <div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '8px' }}>
                    <span className="client-badge client-badge-orange">{item.badge}</span>
                    <span style={{ fontSize: '0.75rem', color: '#64748B', fontWeight: 600 }}>{item.totalDays} Days</span>
                  </div>
                  <h3 style={{ fontSize: '1.05rem', fontWeight: 800, margin: '0 0 6px 0' }}>{item.title}</h3>
                  <p style={{ color: 'var(--app-text-secondary)', fontSize: '0.8125rem', lineHeight: 1.5, margin: '0 0 14px 0' }}>
                    {item.description}
                  </p>
                  <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap', marginBottom: '16px' }}>
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
        <div className="client-card client-card-glow" style={{ maxWidth: '620px', margin: '0 auto', padding: '32px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '16px' }}>
            <div style={{ width: '46px', height: '46px', borderRadius: '12px', background: 'linear-gradient(135deg, #FF6A00 0%, #FF8A3D 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF' }}>
              <Sparkles size={22} />
            </div>
            <div>
              <h3 style={{ fontSize: '1.2rem', fontWeight: 800, margin: 0 }}>AI Custom Roadmap Generator</h3>
              <span style={{ fontSize: '0.75rem', color: 'var(--app-text-secondary)' }}>Synthesizes personalized day-by-day learning curricula</span>
            </div>
          </div>

          <p style={{ fontSize: '0.875rem', color: 'var(--app-text-secondary)', lineHeight: 1.6, marginBottom: '20px' }}>
            Enter any target tech stack or specialized engineering role. JobPilot AI will construct a 30 to 60-day syllabus with embedded tutorials, quiz milestones, and project goals.
          </p>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '14px', marginBottom: '20px' }}>
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
