import React, { createContext, useContext, useState } from 'react';
import api from '../api/apiClient';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => api.getToken());
  const [user, setUser] = useState(() => {
    try {
      const cached = localStorage.getItem('jobpilot_user');
      if (cached) return JSON.parse(cached);
    } catch {
      // fallback
    }
    const defaultUser = {
      fullName: 'Candidate',
      email: 'candidate@jobpilot.app',
      targetRole: 'Android & Full Stack Engineer',
      streak: 3,
      atsScore: 86,
      readinessScore: 88,
      activeRoadmap: 'Python Backend & Microservices',
      roadmapDay: 14,
      totalDays: 36,
    };
    try {
      localStorage.setItem('jobpilot_user', JSON.stringify(defaultUser));
    } catch {
      // ignore
    }
    return defaultUser;
  });
  const [isLoading, setIsLoading] = useState(false);

  const login = async (email, password) => {
    setIsLoading(true);
    try {
      const res = await api.login(email, password);
      const authToken = res.access_token || res.token || 'demo-token';
      api.setToken(authToken);
      setToken(authToken);

      const userData = {
        fullName: res.user?.full_name || email.split('@')[0],
        email: email,
        targetRole: res.user?.target_role || 'Software Engineer',
        streak: res.user?.streak || 3,
        atsScore: 84,
        readinessScore: 88,
        activeRoadmap: 'Python Backend & Microservices',
        roadmapDay: 14,
        totalDays: 36,
      };
      setUser(userData);
      localStorage.setItem('jobpilot_user', JSON.stringify(userData));
      return res;
    } finally {
      setIsLoading(false);
    }
  };

  const startRegistration = async (fullName, email, password) => {
    setIsLoading(true);
    try {
      return await api.registerStart(fullName, email, password);
    } finally {
      setIsLoading(false);
    }
  };

  const verifyRegistration = async (email, code, password, fullName) => {
    setIsLoading(true);
    try {
      const res = await api.registerVerify(email, code, password, fullName);
      const authToken = res.access_token || res.token || 'verified-token';
      api.setToken(authToken);
      setToken(authToken);

      const userData = {
        fullName: fullName || res.user?.full_name || email.split('@')[0],
        email: email,
        targetRole: 'Software Engineer',
        streak: 1,
        atsScore: 80,
        readinessScore: 85,
        activeRoadmap: 'Python Backend & Microservices',
        roadmapDay: 1,
        totalDays: 36,
      };
      setUser(userData);
      localStorage.setItem('jobpilot_user', JSON.stringify(userData));
      return res;
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    api.setToken(null);
    setToken(null);
    setUser(null);
    localStorage.removeItem('jobpilot_user');
  };

  const updateUser = (fields) => {
    setUser((prev) => {
      const updated = { ...(prev || {}), ...fields };
      localStorage.setItem('jobpilot_user', JSON.stringify(updated));
      return updated;
    });
  };

  return (
    <AuthContext.Provider
      value={{
        token,
        user,
        isAuthenticated: true, // Always allow exploration
        isLoading,
        login,
        startRegistration,
        verifyRegistration,
        logout,
        updateUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}

export default AuthContext;
