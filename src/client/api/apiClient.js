/**
 * JobPilot Client API Service
 * Centralized API client communicating directly with the production Render backend.
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

    const response = await fetch(url, {
      ...options,
      headers,
    });

    if (!response.ok) {
      let errorMsg = `Server returned ${response.status}`;
      try {
        const errData = await response.json();
        if (typeof errData.detail === 'string') {
          errorMsg = errData.detail;
        } else if (Array.isArray(errData.detail) && errData.detail[0]?.msg) {
          errorMsg = errData.detail[0].msg;
        } else if (errData.message) {
          errorMsg = errData.message;
        }
      } catch {
        // Ignore json parse error
      }
      throw new Error(errorMsg);
    }

    // Return empty object for 204 No Content
    if (response.status === 204) {
      return {};
    }

    return await response.json();
  }

  // --- HEALTH ---
  async checkHealth() {
    return this.request('/health');
  }

  // --- AUTHENTICATION ---
  async getMe() {
    return this.request('/auth/me');
  }

  async login(email, password) {
    return this.request('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
  }

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

  async registerVerify(email, otp) {
    return this.request('/auth/register/verify', {
      method: 'POST',
      body: JSON.stringify({
        email: email,
        otp: otp,
      }),
    });
  }

  async forgotPasswordStart(email) {
    return this.request('/auth/forgot-password/start', {
      method: 'POST',
      body: JSON.stringify({ email: email }),
    });
  }

  async forgotPasswordVerify(email, otp, newPassword) {
    return this.request('/auth/forgot-password/verify', {
      method: 'POST',
      body: JSON.stringify({
        email: email,
        otp: otp,
        new_password: newPassword,
      }),
    });
  }

  // --- PROFILE ---
  async getProfile() {
    return this.request('/profile');
  }

  async updatePersonalInfo(personalInfo) {
    return this.request('/profile/personal-info', {
      method: 'PUT',
      body: JSON.stringify(personalInfo),
    });
  }

  async addEducation(education) {
    return this.request('/profile/education', {
      method: 'POST',
      body: JSON.stringify(education),
    });
  }

  async deleteEducation(id) {
    return this.request(`/profile/education/${id}`, { method: 'DELETE' });
  }

  async addSkill(skill) {
    return this.request('/profile/skills', {
      method: 'POST',
      body: JSON.stringify(skill),
    });
  }

  async deleteSkill(id) {
    return this.request(`/profile/skills/${id}`, { method: 'DELETE' });
  }

  async addProject(project) {
    return this.request('/profile/projects', {
      method: 'POST',
      body: JSON.stringify(project),
    });
  }

  async deleteProject(id) {
    return this.request(`/profile/projects/${id}`, { method: 'DELETE' });
  }

  async addExperience(experience) {
    return this.request('/profile/experience', {
      method: 'POST',
      body: JSON.stringify(experience),
    });
  }

  async deleteExperience(id) {
    return this.request(`/profile/experience/${id}`, { method: 'DELETE' });
  }

  async addCertification(cert) {
    return this.request('/profile/certifications', {
      method: 'POST',
      body: JSON.stringify(cert),
    });
  }

  async deleteCertification(id) {
    return this.request(`/profile/certifications/${id}`, { method: 'DELETE' });
  }

  async setJobPreferences(preferences) {
    return this.request('/profile/preferences', {
      method: 'POST',
      body: JSON.stringify(preferences),
    });
  }

  // --- ROADMAPS ---
  async getRoadmaps() {
    return this.request('/roadmaps');
  }

  async getCourseCatalog() {
    return this.request('/roadmaps/catalog');
  }

  async getRoadmapSuggestions(query) {
    return this.request(`/roadmaps/suggestions?query=${encodeURIComponent(query)}`);
  }

  async generateRoadmap(role, duration = 6) {
    return this.request('/roadmaps/generate', {
      method: 'POST',
      body: JSON.stringify({ role, duration }),
    });
  }

  async generateRoadmapFromCourses(courseIds) {
    return this.request('/roadmaps/generate-from-courses', {
      method: 'POST',
      body: JSON.stringify({ course_ids: courseIds }),
    });
  }

  async getRoadmapById(id) {
    return this.request(`/roadmaps/${id}`);
  }

  async completeRoadmapDay(dayId) {
    return this.request(`/roadmaps/days/${dayId}/complete`, { method: 'POST' });
  }

  async getDayQuiz(dayId) {
    return this.request(`/roadmaps/days/${dayId}/quiz`);
  }

  async submitDayQuiz(dayId, selectedAnswers) {
    return this.request(`/roadmaps/days/${dayId}/quiz/submit`, {
      method: 'POST',
      body: JSON.stringify({ answers: selectedAnswers }),
    });
  }

  async getDayNote(dayId) {
    return this.request(`/roadmaps/days/${dayId}/note`);
  }

  async saveDayNote(dayId, noteContent) {
    return this.request(`/roadmaps/days/${dayId}/note`, {
      method: 'PUT',
      body: JSON.stringify({ content: noteContent }),
    });
  }

  // --- RESUMES & ATS ---
  async auditMissingFields() {
    return this.request('/resumes/audit/missing-fields');
  }

  async getSavedResumes() {
    return this.request('/resumes/builder/saved');
  }

  async generateResume(title = 'Generated Resume') {
    return this.request('/resumes/builder/generate', {
      method: 'POST',
      body: JSON.stringify({ title }),
    });
  }

  async tailorResume(resumeId, jobTitle, jobDescription) {
    return this.request(`/resumes/builder/${resumeId}/tailor`, {
      method: 'POST',
      body: JSON.stringify({
        job_title: jobTitle,
        job_description: jobDescription,
      }),
    });
  }

  // --- JOBS & APPLICATIONS ---
  async getJobs(query = '', workMode = '') {
    const params = new URLSearchParams();
    if (query) params.append('query', query);
    if (workMode && workMode !== 'All') params.append('work_mode', workMode);
    const qs = params.toString() ? `?${params.toString()}` : '';
    return this.request(`/jobs${qs}`);
  }

  async getJobById(id) {
    return this.request(`/jobs/${id}`);
  }

  async getJobMatch(id) {
    return this.request(`/jobs/${id}/match`);
  }

  async getJobSkillGaps(id) {
    return this.request(`/jobs/${id}/skill-gaps`);
  }

  async getApplications(statusFilter = '') {
    const qs = statusFilter ? `?status_filter=${encodeURIComponent(statusFilter)}` : '';
    return this.request(`/applications${qs}`);
  }

  async createApplication(appData) {
    return this.request('/applications', {
      method: 'POST',
      body: JSON.stringify(appData),
    });
  }

  async updateApplication(id, appData) {
    return this.request(`/applications/${id}`, {
      method: 'PUT',
      body: JSON.stringify(appData),
    });
  }

  async deleteApplication(id) {
    return this.request(`/applications/${id}`, { method: 'DELETE' });
  }

  // --- NOTIFICATIONS ---
  async getNotifications() {
    return this.request('/notifications');
  }

  async markNotificationRead(id) {
    return this.request(`/notifications/${id}/read`, { method: 'PUT' });
  }

  async markAllNotificationsRead() {
    return this.request('/notifications/mark-all-read', { method: 'PUT' });
  }

  // --- INTEGRATIONS ---
  async getIntegrationsStatus() {
    return this.request('/integrations/status');
  }

  async getGitHubRepos() {
    return this.request('/integrations/github/repos');
  }

  async importGitHubRepo(repoName, repoUrl, description, language) {
    return this.request('/integrations/github/import-project', {
      method: 'POST',
      body: JSON.stringify({
        repo_name: repoName,
        repo_url: repoUrl,
        description,
        language,
      }),
    });
  }

  // --- AI ASSISTANT ---
  async getConversations() {
    return this.request('/assistant/conversations');
  }

  async chat(message, conversationId = null) {
    return this.request('/assistant/chat', {
      method: 'POST',
      body: JSON.stringify({
        message,
        conversation_id: conversationId,
      }),
    });
  }

  // --- AI MOCK INTERVIEW ---
  async startInterview(targetRole, mode = 'role') {
    return this.request('/interviews/start', {
      method: 'POST',
      body: JSON.stringify({
        target_role: targetRole,
        mode: mode,
      }),
    });
  }

  async submitInterview(sessionId, answers, facePresenceScore = 95) {
    return this.request(`/interviews/${sessionId}/submit`, {
      method: 'POST',
      body: JSON.stringify({
        answers: answers,
        face_presence_score: facePresenceScore,
      }),
    });
  }

  async getInterviewHistory() {
    return this.request('/interviews/history');
  }

  async getInterviewReport(sessionId) {
    return this.request(`/interviews/${sessionId}/report`);
  }
}

export const api = new ApiClient();
export default api;
