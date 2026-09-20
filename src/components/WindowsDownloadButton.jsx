import React from 'react';
import { Monitor } from 'lucide-react';
import { APP_CONFIG } from '../config/appConfig';

/**
 * Standardized Direct Windows Desktop .exe Download Button
 * Links directly to the standalone executable at APP_CONFIG.EXE_DOWNLOAD_URL
 */
export default function WindowsDownloadButton({
  className = '',
  size = 'md', // 'sm' | 'md' | 'lg'
  variant = 'dark', // 'primary' | 'dark' | 'outline'
  showSubtext = true,
  label = 'Download for Windows',
  subtext = 'Windows 10/11 • Standalone .EXE',
  onClick,
}) {
  const getVariantClass = () => {
    switch (variant) {
      case 'primary':
        return 'btn-exe-primary';
      case 'outline':
        return 'btn-exe-outline';
      case 'dark':
      default:
        return 'btn-exe-dark';
    }
  };

  return (
    <a
      href={APP_CONFIG.EXE_DOWNLOAD_URL}
      download={APP_CONFIG.EXE_FILENAME}
      className={`btn-exe btn-exe-${size} ${getVariantClass()} ${className}`}
      onClick={onClick}
      aria-label={`Download JobPilot Windows Desktop App (${subtext})`}
    >
      <span className="btn-exe-icon" aria-hidden="true">
        <Monitor size={size === 'lg' ? 22 : size === 'sm' ? 16 : 18} />
      </span>

      <div className="btn-exe-text">
        <span className="btn-exe-title">{label}</span>
        {showSubtext && <span className="btn-exe-sub">{subtext}</span>}
      </div>
    </a>
  );
}
