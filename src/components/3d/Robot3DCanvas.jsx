import React, { useEffect, useRef } from 'react';
import * as THREE from 'three';

/**
 * 3D Cybernetic AI Assistant Canvas
 * Interactive Three.js WebGL scene featuring a high-tech robotic copilot
 * with real-time mouse look-at tracking and dynamic disassembly (exploded view)
 * as the user explores different feature domains.
 */
export default function Robot3DCanvas({
  explodedProgress = 0, // 0.0 (assembled) -> 1.0 (fully exploded)
  className = '',
  style = {},
}) {
  const containerRef = useRef(null);
  const progressRef = useRef(explodedProgress);

  useEffect(() => {
    progressRef.current = explodedProgress;
  }, [explodedProgress]);

  useEffect(() => {
    const container = containerRef.current;
    if (!container) return;

    // 1. Scene, Camera & Renderer
    const scene = new THREE.Scene();
    const camera = new THREE.PerspectiveCamera(
      45,
      container.clientWidth / container.clientHeight,
      0.1,
      100
    );
    camera.position.set(0, 0.4, 7.5);

    const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
    renderer.setSize(container.clientWidth, container.clientHeight);
    renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
    renderer.toneMapping = THREE.ACESFilmicToneMapping;
    renderer.toneMappingExposure = 1.2;
    container.appendChild(renderer.domElement);

    // 2. Lighting
    const ambientLight = new THREE.AmbientLight(0x1e293b, 1.5);
    scene.add(ambientLight);

    const keyLight = new THREE.DirectionalLight(0xff6a00, 3.0);
    keyLight.position.set(4, 5, 4);
    scene.add(keyLight);

    const rimLight = new THREE.DirectionalLight(0x38bdf8, 3.5);
    rimLight.position.set(-4, 3, -3);
    scene.add(rimLight);

    const corePointLight = new THREE.PointLight(0xff8533, 4, 8);
    corePointLight.position.set(0, 0.2, 0.6);
    scene.add(corePointLight);

    // 3. Materials
    const metalMaterial = new THREE.MeshStandardMaterial({
      color: 0x1e293b,
      metalness: 0.85,
      roughness: 0.25,
    });

    const darkArmorMaterial = new THREE.MeshStandardMaterial({
      color: 0x0f172a,
      metalness: 0.9,
      roughness: 0.35,
    });

    const orangeAccentMaterial = new THREE.MeshStandardMaterial({
      color: 0xff6a00,
      emissive: 0xff4500,
      emissiveIntensity: 0.6,
      metalness: 0.7,
      roughness: 0.2,
    });

    const glowingVisorMaterial = new THREE.MeshStandardMaterial({
      color: 0x10b981,
      emissive: 0x10b981,
      emissiveIntensity: 1.5,
      roughness: 0.1,
    });

    const coreGlowMaterial = new THREE.MeshStandardMaterial({
      color: 0xffaa00,
      emissive: 0xff6a00,
      emissiveIntensity: 2.2,
      roughness: 0.1,
    });

    // 4. Robot Hierarchy Group
    const robotRoot = new THREE.Group();
    scene.add(robotRoot);

    // A. Head Module
    const headGroup = new THREE.Group();
    headGroup.position.set(0, 1.3, 0);

    // Head Skull
    const headGeo = new THREE.BoxGeometry(0.85, 0.75, 0.85);
    const headMesh = new THREE.Mesh(headGeo, metalMaterial);
    headGroup.add(headMesh);

    // Optic Visor
    const visorGeo = new THREE.BoxGeometry(0.72, 0.2, 0.2);
    const visorMesh = new THREE.Mesh(visorGeo, glowingVisorMaterial);
    visorMesh.position.set(0, 0.08, 0.42);
    headGroup.add(visorMesh);

    // Sensor Antennas
    const antennaGeo = new THREE.CylinderGeometry(0.02, 0.02, 0.4, 8);
    const leftAntenna = new THREE.Mesh(antennaGeo, orangeAccentMaterial);
    leftAntenna.position.set(-0.46, 0.45, 0);
    leftAntenna.rotation.z = -0.2;
    headGroup.add(leftAntenna);

    const rightAntenna = new THREE.Mesh(antennaGeo, orangeAccentMaterial);
    rightAntenna.position.set(0.46, 0.45, 0);
    rightAntenna.rotation.z = 0.2;
    headGroup.add(rightAntenna);

    robotRoot.add(headGroup);

    // B. Torso & Chest Module
    const torsoGroup = new THREE.Group();
    torsoGroup.position.set(0, 0.2, 0);

    const chestGeo = new THREE.BoxGeometry(1.4, 1.2, 0.9);
    const chestMesh = new THREE.Mesh(chestGeo, darkArmorMaterial);
    torsoGroup.add(chestMesh);

    // Arc-Reactor Power Core
    const coreGeo = new THREE.CylinderGeometry(0.24, 0.24, 0.15, 24);
    const coreMesh = new THREE.Mesh(coreGeo, coreGlowMaterial);
    coreMesh.rotation.x = Math.PI / 2;
    coreMesh.position.set(0, 0.15, 0.45);
    torsoGroup.add(coreMesh);

    const coreRingGeo = new THREE.TorusGeometry(0.3, 0.04, 16, 32);
    const coreRing = new THREE.Mesh(coreRingGeo, orangeAccentMaterial);
    coreRing.position.set(0, 0.15, 0.46);
    torsoGroup.add(coreRing);

    robotRoot.add(torsoGroup);

    // C. Left Arm Module
    const leftArmGroup = new THREE.Group();
    leftArmGroup.position.set(-1.1, 0.6, 0);

    const shoulderGeo = new THREE.SphereGeometry(0.28, 16, 16);
    const leftShoulder = new THREE.Mesh(shoulderGeo, metalMaterial);
    leftArmGroup.add(leftShoulder);

    const armSegmentGeo = new THREE.CylinderGeometry(0.12, 0.1, 0.8, 16);
    const leftUpperArm = new THREE.Mesh(armSegmentGeo, darkArmorMaterial);
    leftUpperArm.position.set(-0.15, -0.45, 0);
    leftUpperArm.rotation.z = 0.15;
    leftArmGroup.add(leftUpperArm);

    robotRoot.add(leftArmGroup);

    // D. Right Arm Module
    const rightArmGroup = new THREE.Group();
    rightArmGroup.position.set(1.1, 0.6, 0);

    const rightShoulder = new THREE.Mesh(shoulderGeo, metalMaterial);
    rightArmGroup.add(rightShoulder);

    const rightUpperArm = new THREE.Mesh(armSegmentGeo, darkArmorMaterial);
    rightUpperArm.position.set(0.15, -0.45, 0);
    rightUpperArm.rotation.z = -0.15;
    rightArmGroup.add(rightUpperArm);

    robotRoot.add(rightArmGroup);

    // E. Floating Holographic Rings
    const ring1Geo = new THREE.TorusGeometry(2.1, 0.02, 16, 64);
    const ring1Mat = new THREE.MeshBasicMaterial({ color: 0xff6a00, wireframe: true });
    const ring1 = new THREE.Mesh(ring1Geo, ring1Mat);
    ring1.rotation.x = Math.PI / 3;
    robotRoot.add(ring1);

    const ring2Geo = new THREE.TorusGeometry(2.6, 0.015, 16, 64);
    const ring2Mat = new THREE.MeshBasicMaterial({ color: 0x38bdf8, wireframe: true });
    const ring2 = new THREE.Mesh(ring2Geo, ring2Mat);
    ring2.rotation.y = Math.PI / 4;
    robotRoot.add(ring2);

    // F. Floating Neural Particle Cloud
    const particleCount = 120;
    const particleGeo = new THREE.BufferGeometry();
    const positions = new Float32Array(particleCount * 3);
    for (let i = 0; i < particleCount * 3; i += 3) {
      positions[i] = (Math.random() - 0.5) * 8;
      positions[i + 1] = (Math.random() - 0.5) * 6;
      positions[i + 2] = (Math.random() - 0.5) * 5;
    }
    particleGeo.setAttribute('position', new THREE.BufferAttribute(positions, 3));
    const particleMat = new THREE.PointsMaterial({
      color: 0xff8533,
      size: 0.05,
      transparent: true,
      opacity: 0.8,
    });
    const particles = new THREE.Points(particleGeo, particleMat);
    scene.add(particles);

    // 5. Mouse Interaction Tracking
    let targetMouseX = 0;
    let targetMouseY = 0;
    let currentMouseX = 0;
    let currentMouseY = 0;

    const handleMouseMove = (e) => {
      const rect = container.getBoundingClientRect();
      const x = ((e.clientX - rect.left) / rect.width) * 2 - 1;
      const y = -(((e.clientY - rect.top) / rect.height) * 2 - 1);
      targetMouseX = x * 0.4;
      targetMouseY = y * 0.3;
    };

    window.addEventListener('mousemove', handleMouseMove);

    // 6. Animation Loop
    let animationFrameId;
    let clock = new THREE.Clock();

    const animate = () => {
      animationFrameId = requestAnimationFrame(animate);
      const elapsedTime = clock.getElapsedTime();

      // Smooth mouse damping
      currentMouseX += (targetMouseX - currentMouseX) * 0.05;
      currentMouseY += (targetMouseY - currentMouseY) * 0.05;

      robotRoot.rotation.y = currentMouseX;
      robotRoot.rotation.x = -currentMouseY;

      // Subtle breathing float
      robotRoot.position.y = Math.sin(elapsedTime * 1.5) * 0.08;

      // Arc core pulse
      const pulse = 1 + Math.sin(elapsedTime * 4) * 0.15;
      coreMesh.scale.set(pulse, pulse, 1);
      corePointLight.intensity = 3 + Math.sin(elapsedTime * 4) * 1.5;

      // Orbit rings rotation
      ring1.rotation.z += 0.005;
      ring2.rotation.z -= 0.003;

      // Particles drift
      particles.rotation.y += 0.0008;

      // DYNAMIC DISASSEMBLY / EXPLODED VIEW INTERPOLATION
      const p = progressRef.current; // 0.0 to 1.0

      // Head explodes upwards & tilts back
      headGroup.position.y = 1.3 + p * 1.6;
      headGroup.position.z = -p * 0.3;
      headGroup.rotation.x = p * 0.2;

      // Torso expands forward
      torsoGroup.position.z = p * 0.8;
      torsoGroup.scale.set(1 + p * 0.1, 1 + p * 0.1, 1 + p * 0.1);

      // Left Arm separates left & rotates
      leftArmGroup.position.x = -1.1 - p * 1.8;
      leftArmGroup.position.y = 0.6 + p * 0.4;
      leftArmGroup.rotation.z = p * 0.35;

      // Right Arm separates right & rotates
      rightArmGroup.position.x = 1.1 + p * 1.8;
      rightArmGroup.position.y = 0.6 + p * 0.4;
      rightArmGroup.rotation.z = -p * 0.35;

      // Rings expand in radius
      ring1.scale.set(1 + p * 0.6, 1 + p * 0.6, 1 + p * 0.6);
      ring2.scale.set(1 + p * 0.7, 1 + p * 0.7, 1 + p * 0.7);

      renderer.render(scene, camera);
    };

    animate();

    // 7. Resize Handler
    const handleResize = () => {
      if (!container) return;
      camera.aspect = container.clientWidth / container.clientHeight;
      camera.updateProjectionMatrix();
      renderer.setSize(container.clientWidth, container.clientHeight);
    };

    window.addEventListener('resize', handleResize);

    return () => {
      window.removeEventListener('mousemove', handleMouseMove);
      window.removeEventListener('resize', handleResize);
      cancelAnimationFrame(animationFrameId);
      if (container.contains(renderer.domElement)) {
        container.removeChild(renderer.domElement);
      }
      renderer.dispose();
    };
  }, []);

  return (
    <div
      ref={containerRef}
      className={`robot-3d-canvas-container ${className}`}
      style={{
        position: 'relative',
        width: '100%',
        height: '100%',
        minHeight: '480px',
        overflow: 'hidden',
        pointerEvents: 'none',
        ...style,
      }}
    />
  );
}
