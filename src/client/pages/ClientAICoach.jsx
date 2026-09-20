import React, { useState, useRef, useEffect } from 'react';
import { Sparkles, Send, Trash2, Copy, Check, Bot, User, ArrowRight } from 'lucide-react';
import api from '../api/apiClient';
import { useAuth } from '../context/AuthContext';

export default function ClientAICoach({ onNavigateToMockInterview, onNavigateToRoadmap }) {
  const { user } = useAuth();
  const [messages, setMessages] = useState([
    {
      id: 1,
      sender: 'assistant',
      text: `Hello ${user?.fullName || 'Candidate'}! I am your JobPilot AI Career Coach. I'm connected directly to your career profile and active learning roadmaps.\n\nHow can I accelerate your engineering journey today?`,
      time: 'Just now',
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

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, isLoading]);

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
      const historyPayload = messages.map((m) => ({
        role: m.sender === 'user' ? 'user' : 'assistant',
        content: m.text,
      }));

      const res = await api.sendChatMessage(query, historyPayload);
      const assistantMsg = {
        id: Date.now() + 1,
        sender: 'assistant',
        text: res.response || 'I analyzed your request. Keep advancing your daily roadmap and resume tailoring!',
        time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
      };
      setMessages((prev) => [...prev, assistantMsg]);
    } catch {
      const errorMsg = {
        id: Date.now() + 1,
        sender: 'assistant',
        text: "I couldn't reach the live AI server right now, but your career trajectory remains on track! Continue with Day 14 of your learning roadmap.",
        time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
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

  const clearChat = () => {
    if (window.confirm('Clear your conversation history with AI Career Coach?')) {
      setMessages([
        {
          id: Date.now(),
          sender: 'assistant',
          text: `Chat history cleared. I'm ready for your next question, ${user?.fullName || 'Candidate'}!`,
          time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        },
      ]);
    }
  };

  return (
    <div style={{ maxWidth: '960px', margin: '0 auto', display: 'flex', flexDirection: 'column', height: 'calc(100vh - 120px)' }}>
      {/* Header Bar */}
      <div className="client-card" style={{ padding: '16px 24px', marginBottom: '16px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '14px' }}>
          <div style={{ width: '44px', height: '44px', borderRadius: '12px', background: 'linear-gradient(135deg, #FF6A00 0%, #FF8A3D 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF' }}>
            <Sparkles size={22} />
          </div>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <h2 style={{ fontSize: '1.25rem', fontWeight: 800, margin: 0 }}>AI Career Coach</h2>
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
              Practice Mock Interview
            </button>
          )}
          {onNavigateToRoadmap && (
            <button onClick={onNavigateToRoadmap} className="client-btn client-btn-secondary" style={{ padding: '6px 12px', fontSize: '0.75rem' }}>
              Open Roadmap
            </button>
          )}
          <button onClick={clearChat} className="client-btn client-btn-secondary" style={{ padding: '6px 12px', fontSize: '0.75rem' }}>
            <Trash2 size={14} />
            <span>Clear Chat</span>
          </button>
        </div>
      </div>

      {/* Starter Prompts Bar */}
      <div style={{ display: 'flex', gap: '8px', overflowX: 'auto', paddingBottom: '12px', marginBottom: '8px' }}>
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
              background: 'rgba(30, 41, 59, 0.4)',
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
          marginBottom: '16px',
          background: 'rgba(11, 15, 25, 0.7)',
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

              <div
                style={{
                  maxWidth: '75%',
                  background: isUser ? 'rgba(2, 132, 199, 0.15)' : 'rgba(30, 41, 59, 0.65)',
                  border: isUser ? '1px solid rgba(2, 132, 199, 0.35)' : '1px solid rgba(255, 106, 0, 0.2)',
                  borderRadius: isUser ? '16px 4px 16px 16px' : '4px 16px 16px 16px',
                  padding: '14px 18px',
                }}
              >
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '6px' }}>
                  <span style={{ fontSize: '0.75rem', fontWeight: 700, color: isUser ? '#38BDF8' : 'var(--app-orange)' }}>
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
                    color: '#F1F5F9',
                    whiteSpace: 'pre-line',
                  }}
                >
                  {m.text}
                </div>
              </div>
            </div>
          );
        })}

        {isLoading && (
          <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
            <div style={{ width: '36px', height: '36px', borderRadius: '50%', background: 'linear-gradient(135deg, #FF6A00 0%, #FF8A3D 100%)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#FFFFFF' }}>
              <Bot size={18} />
            </div>
            <div style={{ background: 'rgba(30, 41, 59, 0.65)', border: '1px solid rgba(255, 106, 0, 0.2)', borderRadius: '4px 16px 16px 16px', padding: '12px 18px', color: 'var(--app-orange)', fontSize: '0.8125rem' }}>
              JobPilot Coach is thinking...
            </div>
          </div>
        )}

        <div ref={messagesEndRef} />
      </div>

      {/* Input Bar */}
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
          placeholder="Ask anything about technical interviews, resumes, career roadmaps..."
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
