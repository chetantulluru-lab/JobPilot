export const MOCK_JOBS = [
  {
    id: 'job-1',
    title: 'Python Developer Intern',
    company: 'Nexus Cloud Labs',
    location: 'Remote / Bengaluru',
    type: 'Internship (6 Months)',
    stipend: 'Competitive / Mentorship included',
    matchScore: 94,
    matchTier: 'Exceptional Match',
    summary:
      'Build scalable backend microservices, write clean REST APIs, and collaborate on data ingestion pipelines using Python and modern asynchronous frameworks.',
    requiredSkills: ['Python', 'SQL', 'FastAPI', 'Git', 'Docker'],
    strongMatches: ['Python', 'SQL', 'Git'],
    skillGaps: ['Docker'],
    partialMatches: ['FastAPI (Knowledge of Flask/Django)'],
    aiInsight:
      'Your profile matches 94% of core technical competencies. High proficiency in Python and relational databases makes you an immediate fit; learning Docker basics will close the minor gap.',
  },
  {
    id: 'job-2',
    title: 'AI / ML Research Intern',
    company: 'Cortex Vision AI',
    location: 'Hybrid / Hyderabad',
    type: 'Internship (3-6 Months)',
    stipend: 'Competitive Stipend',
    matchScore: 89,
    matchTier: 'Strong Match',
    summary:
      'Assist in evaluating deep learning architectures, fine-tuning transformer models, and benchmarking dataset pipelines for real-world NLP applications.',
    requiredSkills: ['Python', 'PyTorch', 'NLP', 'Scikit-Learn', 'Pandas'],
    strongMatches: ['Python', 'NLP', 'Pandas', 'Scikit-Learn'],
    skillGaps: ['PyTorch (Profile indicates TensorFlow)'],
    partialMatches: ['Transformers'],
    aiInsight:
      'Strong alignment with NLP fundamentals and tabular modeling. Your transferable skills from TensorFlow will allow rapid onboarding to PyTorch.',
  },
  {
    id: 'job-3',
    title: 'Full Stack Engineer (Junior)',
    company: 'Aether Systems',
    location: 'On-site / Pune',
    type: 'Full-time Entry Level',
    stipend: 'Full Benefits + Equity',
    matchScore: 86,
    matchTier: 'Good Match',
    summary:
      'Develop modern client interfaces in React and implement resilient Node.js services backed by PostgreSQL and Redis caches.',
    requiredSkills: ['React', 'JavaScript / TypeScript', 'Node.js', 'PostgreSQL', 'Tailwind CSS'],
    strongMatches: ['React', 'JavaScript / TypeScript', 'PostgreSQL'],
    skillGaps: ['Node.js (Backend)'],
    partialMatches: ['Tailwind CSS'],
    aiInsight:
      'Your frontend portfolio demonstrates strong React architecture. Pairing your frontend foundation with introductory Node.js backend projects will boost your index.',
  },
];

export const NLP_PIPELINE_STEPS = [
  {
    id: 'step-resume',
    stepNumber: '01',
    name: 'Resume Input',
    subtext: 'Unstructured Documents',
    description: 'Accepts multi-format user career documents (PDF, DOCX, Plaintext).',
    tech: ['PDF Parser', 'Text Extraction', 'OCR Buffer'],
    status: 'Input Layer',
  },
  {
    id: 'step-text-extraction',
    stepNumber: '02',
    name: 'Text Extraction',
    subtext: 'Layout & Stream Parsing',
    description: 'Strips formatting artifacts, resolves multi-column layouts, and extracts raw text streams.',
    tech: ['Layout Detection', 'Regex Cleaning', 'Stream Normalization'],
    status: 'Preprocessing',
  },
  {
    id: 'step-nlp-processing',
    stepNumber: '03',
    name: 'NLP Processing',
    subtext: 'Syntactic & Semantic Tokenization',
    description: 'Applies tokenization, lemmatization, Part-of-Speech (POS) tagging, and Named Entity Recognition.',
    tech: ['Tokenization', 'POS Tagging', 'Named Entity Recognition (NER)'],
    status: 'Core NLP',
  },
  {
    id: 'step-entity-extraction',
    stepNumber: '04',
    name: 'Skills & Experience',
    subtext: 'Ontology Mapping',
    description: 'Classifies entities into verified skills, educational milestones, work history, and project deliverables.',
    tech: ['Skill Taxonomy', 'Entity Classification', 'Timeline Resolution'],
    status: 'Knowledge Extraction',
  },
  {
    id: 'step-career-profile',
    stepNumber: '05',
    name: 'Career Profile',
    subtext: 'Normalized Vector Representation',
    description: 'Constructs a structured JSON career profile and high-dimensional semantic embedding vector.',
    tech: ['Vector Embeddings', 'Structured Schema', 'Profile Strength Engine'],
    status: 'Data Model',
  },
  {
    id: 'step-job-analysis',
    stepNumber: '06',
    name: 'Job Analysis',
    subtext: 'Requirement Extraction',
    description: 'Extracts explicit and implicit requirements from incoming job descriptions across industries.',
    tech: ['Requirement Parsing', 'Seniority Detection', 'Must-Have vs Nice-to-Have'],
    status: 'Target Modeling',
  },
  {
    id: 'step-semantic-matching',
    stepNumber: '07',
    name: 'Semantic Matching',
    subtext: 'Relevance Scoring',
    description: 'Calculates cosine similarity between profile and role vectors while applying weighted rule adjustments.',
    tech: ['Semantic Similarity', 'Cosine Distance', 'Skill Gap Diagnosis'],
    status: 'Inference',
  },
  {
    id: 'step-match-score',
    stepNumber: '08',
    name: 'Match Score & Insights',
    subtext: 'Actionable Candidate Output',
    description: 'Produces the 0-100% match score with transparent strong matches and prioritized gap recommendations.',
    tech: ['Score Calibration', 'Explainability Matrix', 'Actionable Insights'],
    status: 'Output Layer',
  },
];

export const NLP_CONCEPTS = [
  {
    title: 'Natural Language Processing',
    description:
      'JobPilot is designed to use NLP to transform unstructured text in resumes and job postings into machine-readable knowledge representations.',
    badge: 'Foundation',
  },
  {
    title: 'Text Classification',
    description:
      'Categorizes resume text sections (experience, education, projects, certifications) to accurately interpret the candidate journey.',
    badge: 'Structure',
  },
  {
    title: 'Keyword & Entity Extraction',
    description:
      'Identifies technical proficiencies, frameworks, methodologies, degrees, and organizations using domain-specific entity models.',
    badge: 'NER Models',
  },
  {
    title: 'Semantic Similarity',
    description:
      'Goes beyond exact word matches: understands that "FastAPI" relates to "Python REST APIs" and "AWS" relates to "Cloud Infrastructure".',
    badge: 'Embeddings',
  },
  {
    title: 'Machine Learning Ranking',
    description:
      'Balances required vs preferred qualifications to provide candidates with transparent reasons why an opportunity fits them.',
    badge: 'Ranking Engine',
  },
];
