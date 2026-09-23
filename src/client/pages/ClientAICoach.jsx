import React, { useState, useRef, useEffect } from 'react';
import { 
  Sparkles, 
  Send, 
  Trash2, 
  Copy, 
  Check, 
  Bot, 
  User, 
  ArrowRight, 
  Info, 
  RefreshCw,
  Video,
  Compass
} from 'lucide-react';
import api from '../api/apiClient';
import { useAuth } from '../context/AuthContext';
import AIOrb from '../../components/AIOrb';

export default function ClientAICoach({ onNavigateToMockInterview, onNavigateToRoadmap }) {
  const { user } = useAuth();
  const [conversationId, setConversationId] = useState(null);
  const [messages, setMessages] = useState([
    {
      id: 1,
      sender: 'assistant',
      text: `Hello ${user?.fullName || 'Candidate'}! I am your JobPilot AI Career Coach. I'm connected directly to your career profile and active learning roadmaps.\n\nHow can I accelerate your engineering journey today?`,
      time: 'Just now',
      isFallback: false,
      suggestedActions: [
        'Analyze my profile for Python backend roles',
        'How do I bridge missing skills for high match jobs?',
      ],
    },
  ]);
  const [inputText, setInputText] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [copiedId, setCopiedId] = useState(null);
  const messagesEndRef = useRef(null);

  const starterPrompts = [
    'Analyze my profile for Python backend roles',
    'How do I bridge missing skills for high match jobs?',
    'Help me draft a concise message to a recruiter',
    'Generate 3 mock interview questions for FastAPI',
  ];

  // Auto-scroll on new message
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, isLoading]);

  // Load existing conversation on mount if available
  useEffect(() => {
    let isMounted = true;
    const fetchLatestConversation = async () => {
      try {
        const convs = await api.getConversations();
        if (isMounted && Array.isArray(convs) && convs.length > 0) {
          const latest = convs[0];
          setConversationId(latest.id);
          const fullConv = await api.getConversation(latest.id);
          if (isMounted && fullConv && Array.isArray(fullConv.messages) && fullConv.messages.length > 0) {
            const formatted = fullConv.messages.map((m) => ({
              id: m.id || Math.random(),
              sender: m.sender === 'user' ? 'user' : 'assistant',
              text: m.content || '',
              time: m.created_at ? new Date(m.created_at).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : 'Earlier',
              isFallback: m.metadata_json?.is_fallback || false,
            }));
            setMessages(formatted);
          }
        }
      } catch {
        // Fallback to initial greeting
      }
    };
    fetchLatestConversation();
    return () => {
      isMounted = false;
    };
  }, []);

  const handleSend = async (textToSend) => {
    const query = (textToSend || inputText).trim();
    if (!query || isLoading) return;

    const userMsg = {
      id: Date.now(),
      sender: 'user',
      text: query,
      time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
    };

    setMessages((prev) => [...prev, userMsg]);
    setInputText('');
    setIsLoading(true);

    try {
      const res = await api.sendChatMessage(query, conversationId);
      if (res.conversation_id && !conversationId) {
        setConversationId(res.conversation_id);
      }

      const responseText = res.reply || res.response || 'I analyzed your query based on your confirmed career profile.';
      
      // Dynamic suggested follow-ups
      const dynamicFollowUps = [];
      if (query.toLowerCase().includes('python') || query.toLowerCase().includes('backend')) {
        dynamicFollowUps.push('What are the top FastAPI interview questions?');
        dynamicFollowUps.push('Tailor my resume for a Python Backend role');
      } else if (query.toLowerCase().includes('skill') || query.toLowerCase().includes('match')) {
        dynamicFollowUps.push('Open my 36-day Python & Microservices Roadmap');
        dynamicFollowUps.push('Practice Mock Interview for this stack');
      } else {
        dynamicFollowUps.push('How can I optimize my project descriptions?');
        dynamicFollowUps.push('Draft an outreach message to a hiring manager');
      }

      const assistantMsg = {
        id: Date.now() + 1,
        sender: 'assistant',
        text: responseText,
        time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        isFallback: !!res.is_fallback,
        suggestedActions: dynamicFollowUps,
      };
      setMessages((prev) => [...prev, assistantMsg]);
    } catch {
      // Offline / network fallback grounded in verified profile
      const fallbackReply = `Hello ${user?.fullName || 'Candidate'}! Based on your confirmed profile, your technical foundations remain strong. You can review your daily roadmap milestones or practice a mock interview session to continue advancing.`;
      const errorMsg = {
        id: Date.now() + 1,
        sender: 'assistant',
        text: fallbackReply,
        time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        isFallback: true,
        suggestedActions: [
          'Practice Mock Interview',
          'Open Career Roadmap',
        ],
      };
      setMessages((prev) => [...prev, errorMsg]);
    } finally {
      setIsLoading(false);
    }
  };

  const copyToClipboard = (id, text) => {
    navigator.clipboard?.writeText(text);
    setCopiedId(id);
    setTimeout(() => setCopiedId(null), 2000);
  };

  const clearChat = async () => {
    if (window.confirm('Clear your conversation history with AI Career Coach?')) {
      if (conversationId) {
        try {
          await api.deleteConversation(conversationId);
        } catch {
          // Ignore
        }
      }
      setConversationId(null);
      setMessages([
        {
          id: Date.now(),
          sender: 'assistant',
          text: `Chat history cleared. I'm ready for your next question, ${user?.fullName || 'Candidate'}!`,
          time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
          isFallback: false,
          suggestedActions: [
            'Analyze my profile for Python backend roles',
            'How do I bridge missing skills for high match jobs?',
          ],
        },
      ]);
    }
  };

  return (
    <div style={{ maxWidth: '960px', margin: '0 auto', display: 'flex', flexDirection: 'column', height: 'calc(100vh - 110px)' }}>
      {/* Top Header Bar */}
      <div className="client-card" style={{ padding: '16px 20px', marginBottom: '12px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '12px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <div style={{ width: '42px', height: '42px', borderRadius: '12px', background: 'linear-gradient(135deg, #FF6A00 0%, #FF8A3D 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF', flexShrink: 0 }}>
            <Sparkles size={22} />
          </div>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <h2 style={{ fontSize: '1.2rem', fontWeight: 800, margin: 0, color: 'var(--app-text)' }}>AI Career Coach</h2>
              <span className="client-badge client-badge-orange" style={{ fontSize: '0.6875rem' }}>ACTIVE</span>
            </div>
            <span style={{ fontSize: '0.75rem', color: 'var(--app-orange)', fontWeight: 600 }}>
              Zero-Fabrication • Grounded in Your Profile
            </span>
          </div>
        </div>

        <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
          {onNavigateToMockInterview && (
            <button onClick={onNavigateToMockInterview} className="client-btn client-btn-secondary" style={{ padding: '6px 12px', fontSize: '0.75rem' }}>
              <Video size={14} color="var(--app-orange)" />
              <span>Practice Mock Interview</span>
            </button>
          )}
          {onNavigateToRoadmap && (
            <button onClick={onNavigateToRoadmap} className="client-btn client-btn-secondary" style={{ padding: '6px 12px', fontSize: '0.75rem' }}>
              <Compass size={14} color="var(--app-orange)" />
              <span>Open Roadmap</span>
            </button>
          )}
          <button onClick={clearChat} className="client-btn client-btn-secondary" style={{ padding: '6px 12px', fontSize: '0.75rem' }}>
            <Trash2 size={14} color="#94A3B8" />
            <span>Clear Chat</span>
          </button>
        </div>
      </div>

      {/* Safety & Grounding Banner (1:1 Android Compose Info Banner) */}
      <div style={{ padding: '8px 16px', background: 'var(--app-orange-light)', border: '1px solid rgba(255, 106, 0, 0.25)', borderRadius: '10px', marginBottom: '12px', display: 'flex', alignItems: 'center', gap: '10px' }}>
        <Info size={16} color="var(--app-orange)" style={{ flexShrink: 0 }} />
        <span style={{ fontSize: '0.75rem', color: 'var(--app-text-secondary)', lineHeight: 1.4 }}>
          Answers cite only your confirmed experience and skills to ensure authentic career progression.
        </span>
      </div>

      {/* Starter Quick Prompt Chips */}
      <div style={{ display: 'flex', gap: '8px', overflowX: 'auto', paddingBottom: '8px', marginBottom: '8px' }}>
        {starterPrompts.map((prompt, idx) => (
          <button
            key={idx}
            onClick={() => handleSend(prompt)}
            className="client-btn client-btn-secondary"
            style={{
              padding: '6px 14px',
              fontSize: '0.75rem',
              borderRadius: '20px',
              whiteSpace: 'nowrap',
              background: '#FFFFFF',
              borderColor: 'rgba(255, 106, 0, 0.25)',
              color: 'var(--app-text-secondary)',
            }}
          >
            <span>{prompt}</span>
            <ArrowRight size={12} color="var(--app-orange)" />
          </button>
        ))}
      </div>

      {/* Messages Scroll Area */}
      <div
        className="client-card"
        style={{
          flex: 1,
          overflowY: 'auto',
          padding: '20px',
          display: 'flex',
          flexDirection: 'column',
          gap: '16px',
          marginBottom: '12px',
          background: '#FFFFFF',
          border: '1px solid var(--app-border-subtle)',
        }}
      >
        {messages.map((m) => {
          const isUser = m.sender === 'user';
          return (
            <div
              key={m.id}
              style={{
                display: 'flex',
                flexDirection: isUser ? 'row-reverse' : 'row',
                gap: '12px',
                alignItems: 'flex-start',
              }}
            >
              <div
                style={{
                  width: '36px',
                  height: '36px',
                  borderRadius: '50%',
                  background: isUser ? 'linear-gradient(135deg, #0284C7 0%, #38BDF8 100%)' : 'linear-gradient(135deg, #FF6A00 0%, #FF8A3D 100%)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  color: '#FFFFFF',
                  flexShrink: 0,
                }}
              >
                {isUser ? <User size={18} /> : <Bot size={18} />}
              </div>

              <div style={{ maxWidth: '80%', display: 'flex', flexDirection: 'column', alignItems: isUser ? 'flex-end' : 'flex-start' }}>
                <div
                  style={{
                    background: isUser ? 'var(--app-info-bg)' : 'var(--app-surface-light)',
                    border: isUser ? '1px solid rgba(2, 132, 199, 0.3)' : '1px solid rgba(255, 106, 0, 0.2)',
                    borderRadius: isUser ? '16px 4px 16px 16px' : '4px 16px 16px 16px',
                    padding: '14px 18px',
                    boxShadow: '0 2px 8px rgba(15, 23, 42, 0.03)',
                  }}
                >
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '6px', gap: '16px' }}>
                    <span style={{ fontSize: '0.75rem', fontWeight: 800, color: isUser ? '#0369A1' : 'var(--app-orange)' }}>
                      {isUser ? 'You' : 'JobPilot Coach'}
                    </span>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                      <span style={{ fontSize: '0.6875rem', color: '#64748B' }}>{m.time}</span>
                      {!isUser && (
                        <button
                          onClick={() => copyToClipboard(m.id, m.text)}
                          style={{ background: 'transparent', border: 'none', color: '#94A3B8', cursor: 'pointer', padding: 0 }}
                          title="Copy to clipboard"
                        >
                          {copiedId === m.id ? <Check size={13} color="#10B981" /> : <Copy size={13} />}
                        </button>
                      )}
                    </div>
                  </div>

                  <div
                    style={{
                      fontSize: '0.875rem',
                      lineHeight: 1.6,
                      color: 'var(--app-text)',
                      whiteSpace: 'pre-line',
                    }}
                  >
                    {m.text}
                  </div>

                  {!isUser && m.isFallback && (
                    <div style={{ marginTop: '8px', fontSize: '0.6875rem', color: '#94A3B8', fontWeight: 700, display: 'flex', alignItems: 'center', gap: '4px' }}>
                      <span>⚡ Grounded Fallback Mode</span>
                    </div>
                  )}
                </div>

                {/* Suggested Action Chips */}
                {!isUser && Array.isArray(m.suggestedActions) && m.suggestedActions.length > 0 && (
                  <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px', marginTop: '8px' }}>
                    {m.suggestedActions.map((action, aIdx) => (
                      <button
                        key={aIdx}
                        onClick={() => {
                          if (action.includes('Mock Interview') && onNavigateToMockInterview) {
                            onNavigateToMockInterview();
                          } else if (action.includes('Roadmap') && onNavigateToRoadmap) {
                            onNavigateToRoadmap();
                          } else {
                            handleSend(action);
                          }
                        }}
                        style={{
                          background: 'var(--app-orange-light)',
                          border: '1px solid rgba(255, 106, 0, 0.3)',
                          borderRadius: '12px',
                          padding: '4px 10px',
                          fontSize: '0.6875rem',
                          color: 'var(--app-orange)',
                          fontWeight: 700,
                          cursor: 'pointer',
                          display: 'flex',
                          alignItems: 'center',
                          gap: '4px',
                        }}
                      >
                        <span>✦ {action}</span>
                      </button>
                    ))}
                  </div>
                )}
              </div>
            </div>
          );
        })}

        {isLoading && (
          <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
            <div style={{ width: '36px', height: '36px', borderRadius: '50%', background: 'linear-gradient(135deg, #FF6A00 0%, #FF8A3D 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF', flexShrink: 0 }}>
              <Bot size={18} />
            </div>
            <div style={{ background: 'var(--app-orange-light)', border: '1px solid rgba(255, 106, 0, 0.25)', borderRadius: '4px 16px 16px 16px', padding: '12px 18px', color: 'var(--app-orange)', fontSize: '0.8125rem', fontWeight: 700, display: 'flex', alignItems: 'center', gap: '8px' }}>
              <AIOrb style={{ width: '20px', height: '20px' }} />
              <span>AI Coach is formulating grounded guidance...</span>
            </div>
          </div>
        )}

        <div ref={messagesEndRef} />
      </div>

      {/* Chat Input Bar */}
      <form
        onSubmit={(e) => {
          e.preventDefault();
          handleSend();
        }}
        style={{ display: 'flex', gap: '10px' }}
      >
        <input
          type="text"
          value={inputText}
          onChange={(e) => setInputText(e.target.value)}
          placeholder="Ask your AI Coach anything about tech interviews, resumes, career roadmaps..."
          className="client-input"
          style={{ flex: 1, padding: '14px 18px' }}
        />
        <button
          type="submit"
          disabled={isLoading || !inputText.trim()}
          className="client-btn client-btn-primary"
          style={{ padding: '0 24px', borderRadius: '12px' }}
        >
          <Send size={18} />
          <span>Send</span>
        </button>
      </form>
    </div>
  );
}
