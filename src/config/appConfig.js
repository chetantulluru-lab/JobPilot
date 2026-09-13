/**
 * JobPilot Central Application Configuration
 *
 * NOTE: The Android application is the primary product.
 * When the Android app is published on the Google Play Store,
 * update PLAY_STORE_URL with the actual Google Play Store link.
 * All download buttons and CTAs throughout the website reference this single source of truth.
 */

export const APP_CONFIG = {
  name: 'JobPilot',
  symbol: '✦',
  tagline: 'Your career. Piloted by AI.',
  description:
    'JobPilot uses AI and natural language processing to understand your career profile, discover relevant opportunities, and help you navigate your job search smarter.',
  version: '1.0.0-phase1a',
  minAndroidVersion: 'Android 11+ (API Level 30)',

  // Centralized Direct APK Download configuration
  APK_DOWNLOAD_URL: '/downloads/JobPilot.apk',
  APK_FILENAME: 'JobPilot.apk',
  APK_FILE_SIZE: '44 MB',

  // Centralized Play Store configuration
  // Safe placeholder until official Google Play Store listing is live
  PLAY_STORE_URL: '#',

  // Contact & Social placeholders
  supportEmail: 'contact@jobpilot.app',
  routes: {
    home: '/',
    features: '/features',
    howItWorks: '/how-it-works',
    about: '/about',
    download: '/download',
    privacy: '/privacy',
    terms: '/terms',
    contact: '/contact',
  },
};

export default APP_CONFIG;
