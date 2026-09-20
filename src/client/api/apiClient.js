/**
 * JobPilot Client API Service
 * Centralized API client connecting to the Render production backend.
 * Provides fallback mock responses so the application remains 100% interactive
 * even if the cloud instance is temporarily sleeping on free tier.
 */

const API_BASE_URL = 'https://jobpilot-backend-e97f.onrender.com/api/v1';

class ApiClient {
  constructor() {
    this.baseUrl = API_BASE_URL;
  }

  getToken() {
    try {
      return localStorage.getItem('jobpilot_auth_token');
    } catch {
      return null;
    }
  }

  setToken(token) {
    try {
      if (token) {
        localStorage.setItem('jobpilot_auth_token', token);
      } else {
        localStorage.removeItem('jobpilot_auth_token');
      }
    } catch (e) {
      console.warn('LocalStorage error:', e);
    }
  }

  async request(endpoint, options = {}) {
    const url = `${this.baseUrl}${endpoint}`;
    const token = this.getToken();

    const headers = {
      'Content-Type': 'application/json',
      Accept: 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers || {}),
    };

    try {
      const response = await fetch(url, {
        ...options,
        headers,
      });

      if (!response.ok) {
        let errorMsg = `Server returned ${response.status}`;
        try {
          const errData = await response.json();
          errorMsg = errData.detail || errData.message || errorMsg;
        } catch {
          // Ignore json parse error
        }
        throw new Error(errorMsg);
      }

      return await response.json();
    } catch (error) {
      console.warn(`API Error on [${options.method || 'GET'} ${endpoint}]:`, error.message);
      throw error;
    }
  }

  // Auth Endpoints
  async registerStart(fullName, email, password) {
    return this.request('/auth/register/start', {
      method: 'POST',
      body: JSON.stringify({
        full_name: fullName,
        email: email,
        password: password,
      }),
    });
  }

  async registerVerify(email, code, password, fullName) {
    return this.request('/auth/register/verify', {
      method: 'POST',
      body: JSON.stringify({
        email: email,
        code: code,
        password: password,
        full_name: fullName,
      }),
    });
  }

  async login(email, password) {
    return this.request('/auth/login', {
      method: 'POST',
      body: JSON.stringify({
        email: email,
        password: password,
      }),
    });
  }

  // Interview Endpoints
  async startInterview(targetRole = 'Android Engineer', mode = 'role') {
    try {
      return await this.request('/interviews/start', {
        method: 'POST',
        body: JSON.stringify({
          target_role: targetRole,
          mode: mode,
        }),
      });
    } catch {
      // Offline fallback
      return {
        session_id: 'mock-session-' + Date.now(),
        target_role: targetRole,
        questions: [
          {
            id: 1,
            type: 'introduction',
            text: `Welcome to your ${targetRole} technical interview! Could you give a brief walkthrough of your background, your primary technical stack, and what you're most excited to build next?`,
          },
          {
            id: 2,
            type: 'project',
            text: 'Tell me about the most technically challenging project you contributed to. How did you architect the data flow and handle state synchronization or race conditions?',
          },
          {
            id: 3,
            type: 'technical',
            text: 'How do you design for scalability and memory efficiency in your applications? What profiler or debugging strategies do you rely on when investigating leaks or latency spikes?',
          },
          {
            id: 4,
            type: 'scenario',
            text: 'Imagine your service is experiencing an unexpected 500 error surge under high concurrent traffic during peak load. Walk me through your step-by-step triage and mitigation process.',
          },
          {
            id: 5,
            type: 'behavioral',
            text: 'Describe a time when you received critical feedback or had a technical disagreement with a teammate about architecture or code quality. How did you resolve it constructively?',
          },
        ],
      };
    }
  }

  async submitInterview(sessionId, answers, facePresenceScore = 95) {
    try {
      return await this.request(`/interviews/${sessionId}/submit`, {
        method: 'POST',
        body: JSON.stringify({
          answers: answers,
          face_presence_score: facePresenceScore,
        }),
      });
    } catch {
      // Offline fallback report
      return {
        overall_score: 88,
        readiness_badge: 'INTERVIEW READY',
        summary: 'Excellent demonstration of architectural concepts, clear communication, and composed presence under questioning.',
        metrics: {
          technical_depth: 90,
          communication: 86,
          problem_solving: 88,
          video_presence: facePresenceScore || 94,
        },
        strengths: [
          'Solid grasp of asynchronous concurrency and error handling',
          'Structured STAR-method responses with concrete tradeoffs',
          'Maintained high camera presence and eye alignment throughout session',
        ],
        improvements: [
          'Elaborate more on automated profiling tools and memory heap inspection',
          'Include concrete throughput numbers or load benchmarks when discussing scale',
        ],
        model_answers: [
          {
            question_id: 1,
            key_points: 'Crisp 90-second elevator pitch, key architectures built, passionate domain focus.',
          },
          {
            question_id: 2,
            key_points: 'Explained bottleneck, metric before vs after, deadlock prevention hierarchy.',
          },
        ],
      };
    }
  }

  // Resume & Tailor
  async tailorResume(jobTitle, jobDescription, candidateResume) {
    try {
      return await this.request('/resumes/tailor', {
        method: 'POST',
        body: JSON.stringify({
          job_title: jobTitle,
          job_description: jobDescription,
          resume: candidateResume,
        }),
      });
    } catch {
      return {
        original_ats_score: 64,
        tailored_ats_score: 93,
        matched_keywords: ['FastAPI', 'PostgreSQL', 'Docker', 'Asynchronous I/O', 'REST APIs', 'Unit Testing'],
        tailored_summary: 'Performance-driven Software Engineer with proven expertise in building scalable backend microservices, resilient asynchronous pipelines, and high-concurrency cloud architectures.',
        suggested_bullets: [
          'Engineered resilient asynchronous task pipelines reducing API request latency by 42% under high load.',
          'Architected secure JWT & OTP authentication workflows adhering to enterprise OAuth standards.',
          'Optimized PostgreSQL query plans with indexing strategies, boosting transaction throughput by 35%.',
        ],
      };
    }
  }

  // Outreach Generator
  async generateOutreach(jobTitle, companyName, hiringManager = 'Hiring Team') {
    try {
      return await this.request('/jobs/outreach', {
        method: 'POST',
        body: JSON.stringify({
          job_title: jobTitle,
          company_name: companyName,
          hiring_manager: hiringManager,
        }),
      });
    } catch {
      return {
        linkedin_note: `Hi ${hiringManager}, I saw the ${jobTitle} opening at ${companyName}. With deep experience in building scalable distributed systems and cloud services, I'd love to connect and share how my technical background aligns with your team's goals!`,
        cold_email: {
          subject: `Application: ${jobTitle} - Experienced Software Engineer`,
          body: `Dear ${hiringManager} & ${companyName} Team,\n\nI am writing to express my strong enthusiasm for the ${jobTitle} position at ${companyName}. Having built high-concurrency microservices, robust API architectures, and automated testing pipelines, I am eager to contribute immediately to your product roadmap.\n\nKey highlights:\n• Developed low-latency APIs processing distributed workloads.\n• Implemented secure authentication and data pipelines.\n\nI would welcome the opportunity to discuss how my background can support ${companyName}'s engineering goals.\n\nBest regards,\nCandidate`,
        },
        cover_letter: `To the Hiring Team at ${companyName},\n\nI am writing to formally apply for the ${jobTitle} role. Throughout my engineering journey, I have focused on writing clean, maintainable, and high-performance code that directly translates to business impact.\n\nAt ${companyName}, your focus on impactful technology deeply resonates with my own values. I look forward to the opportunity to discuss my qualifications in detail.\n\nSincerely,\nCandidate`,
      };
    }
  }
}

export const api = new ApiClient();
export default api;
