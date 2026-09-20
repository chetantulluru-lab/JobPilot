import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../api/apiClient';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => api.getToken());
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [onboardingDone, setOnboardingDone] = useState(() => {
    try {
      return localStorage.getItem('jobpilot_onboarding_completed') === 'true';
    } catch {
      return false;
    }
  });

  // Verify and restore session against live Render backend on startup
  useEffect(() => {
    let isMounted = true;
    const restoreSession = async () => {
      const storedToken = api.getToken();
      if (!storedToken) {
        if (isMounted) {
          setUser(null);
          setIsLoading(false);
        }
        return;
      }

      try {
        const me = await api.getMe();
        if (isMounted) {
          setUser({
            id: me.id,
            fullName: me.full_name || me.fullName || me.email?.split('@')[0] || 'User',
            email: me.email,
            targetRole: me.target_role || 'Software Engineer',
            streak: me.streak || 1,
            profileStrength: me.profile_strength || 0,
          });
        }
      } catch (err) {
        console.warn('Session restoration failed or expired:', err.message);
        api.setToken(null);
        if (isMounted) {
          setToken(null);
          setUser(null);
        }
      } finally {
        if (isMounted) {
          setIsLoading(false);
        }
      }
    };

    restoreSession();
    return () => {
      isMounted = false;
    };
  }, []);

  const login = async (email, password) => {
    setIsLoading(true);
    try {
      const res = await api.login(email, password);
      const authToken = res.access_token || res.token;
      if (!authToken) {
        throw new Error('No access token received from authentication server.');
      }
      api.setToken(authToken);
      setToken(authToken);

      const me = await api.getMe();
      const userData = {
        id: me.id,
        fullName: me.full_name || me.fullName || email.split('@')[0],
        email: email,
        targetRole: me.target_role || 'Software Engineer',
        streak: me.streak || 1,
        profileStrength: me.profile_strength || 0,
      };
      setUser(userData);
      return res;
    } finally {
      setIsLoading(false);
    }
  };

  const startRegistration = async (fullName, email, password) => {
    return await api.registerStart(fullName, email, password);
  };

  const verifyRegistration = async (email, code, password, fullName) => {
    setIsLoading(true);
    try {
      const res = await api.registerVerify(email, code);
      const authToken = res.access_token || res.token;
      if (authToken) {
        api.setToken(authToken);
        setToken(authToken);

        const me = await api.getMe();
        const userData = {
          id: me.id,
          fullName: me.full_name || fullName || email.split('@')[0],
          email: email,
          targetRole: 'Software Engineer',
          streak: 1,
          profileStrength: 0,
        };
        setUser(userData);
      }
      return res;
    } finally {
      setIsLoading(false);
    }
  };

  const startForgotPassword = async (email) => {
    return await api.forgotPasswordStart(email);
  };

  const verifyForgotPassword = async (email, code, newPassword) => {
    return await api.forgotPasswordVerify(email, code, newPassword);
  };

  const completeOnboarding = () => {
    try {
      localStorage.setItem('jobpilot_onboarding_completed', 'true');
    } catch {
      // ignore
    }
    setOnboardingDone(true);
  };

  const logout = () => {
    api.setToken(null);
    setToken(null);
    setUser(null);
    try {
      localStorage.removeItem('jobpilot_auth_token');
      localStorage.removeItem('jobpilot_user');
    } catch {
      // ignore
    }
  };

  const updateUser = (fields) => {
    setUser((prev) => (prev ? { ...prev, ...fields } : fields));
  };

  return (
    <AuthContext.Provider
      value={{
        token,
        user,
        isAuthenticated: !!user,
        isLoading,
        onboardingDone,
        completeOnboarding,
        login,
        startRegistration,
        verifyRegistration,
        startForgotPassword,
        verifyForgotPassword,
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
