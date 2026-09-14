"""
Comprehensive Computer Science & Engineering Course Catalog for JobPilot.
Contains 100+ subject topics across Core CSE, Software Development tracks,
AI/ML, Web/Mobile, and DevOps/Cloud.
"""

from typing import Dict, List, Any

CSE_COURSE_CATALOG: Dict[str, Dict[str, Any]] = {
    "dsa-cse": {
        "id": "dsa-cse",
        "title": "Data Structures & Algorithms (DSA)",
        "category": "Core CSE",
        "badge": "Core Foundation",
        "description": "Master algorithmic problem solving, time/space complexity, linear & non-linear data structures, trees, graphs, and dynamic programming.",
        "skills": ["Algorithms", "Data Structures", "Dynamic Programming", "Graph Theory", "LeetCode Problem Solving", "Big-O Notation"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Complexity Analysis, Arrays & Linked Lists",
                "description": "Foundations of Big-O notation, array manipulation, pointers, and singly/doubly linked lists.",
                "project_title": "Memory-Efficient Dynamic Array & Doubly Linked List Engine",
                "project_description": "Implement custom dynamic arrays and doubly linked lists with automated stress tests.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Asymptotic Analysis & Big-O Notation",
                        "learning_objective": "Analyze algorithmic runtimes: Best, Average, Worst case, Space-Time trade-offs.",
                        "subtopics": ["Big-O, Big-Omega, Big-Theta", "Time & Space complexity calculation", "Loop analysis patterns"],
                        "practice_tasks": ["Calculate time complexity of 5 nested loop algorithms", "Compare recursive vs iterative Fibonacci space complexity"],
                        "resources": [
                            {"title": "Big-O Complexity Guide", "url": "https://www.freecodecamp.org/news/big-o-notation-why-it-matters-and-why-it-doesnt-163ac98ec1b7/", "language": "English", "resource_type": "article", "source": "FreeCodeCamp"},
                            {"title": "Time and Space Complexity in Telugu", "url": "https://www.youtube.com/results?search_query=time+complexity+dsa+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Array Fundamentals & Two-Pointer Techniques",
                        "learning_objective": "Master in-place array transformations and the two-pointer paradigm.",
                        "subtopics": ["Two-pointer pattern", "Prefix sums", "Kadane's Algorithm for max subarray"],
                        "practice_tasks": ["Solve Two Sum on sorted array", "Implement Maximum Subarray (Kadane's Algorithm)"],
                        "resources": [
                            {"title": "Two Pointer Technique Overview", "url": "https://leetcode.com/explore/learn/card/array-and-string/", "language": "English", "resource_type": "doc", "source": "LeetCode"}
                        ]
                    },
                    {
                        "day_number": 3,
                        "topic": "Singly & Doubly Linked Lists",
                        "learning_objective": "Build, reverse, and manipulate linked data structures with pointer mechanics.",
                        "subtopics": ["Node allocation", "Reversing linked list", "Fast & slow pointer cycle detection"],
                        "practice_tasks": ["Reverse a singly linked list in-place", "Detect cycle using Floyd's algorithm"],
                        "resources": [
                            {"title": "Linked List Data Structure", "url": "https://www.geeksforgeeks.org/data-structures/linked-list/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    },
                    {
                        "day_number": 4,
                        "topic": "Stacks & Queues with Monotonic Patterns",
                        "learning_objective": "Understand LIFO and FIFO primitives and solve next greater element queries.",
                        "subtopics": ["Stack using array/list", "Queue using ring buffer", "Monotonic stack pattern"],
                        "practice_tasks": ["Implement Min-Stack with O(1) getMin()", "Solve Next Greater Element with monotonic stack"],
                        "resources": [
                            {"title": "Stacks and Queues Masterclass", "url": "https://www.youtube.com/results?search_query=stacks+and+queues+dsa", "language": "English", "resource_type": "video", "source": "YouTube"}
                        ]
                    },
                    {
                        "day_number": 5,
                        "topic": "Recursion & Backtracking Foundations",
                        "learning_objective": "Build recursive mental models, understand the call stack, and solve combinatorial search problems.",
                        "subtopics": ["Base cases and recurrence relations", "Subset generation", "Permutations with backtracking"],
                        "practice_tasks": ["Generate all valid combinations of parentheses", "Solve N-Queens or Subset Sum"],
                        "resources": [
                            {"title": "Recursion and Backtracking Visualized", "url": "https://visualgo.net/en/recursion", "language": "English", "resource_type": "article", "source": "VisuAlgo"}
                        ]
                    }
                ]
            },
            {
                "phase_number": 2,
                "title": "Trees, Graphs & Dynamic Programming",
                "description": "Non-linear hierarchies, graph traversals (BFS/DFS, Dijkstra), and optimal substructure memoization.",
                "project_title": "Network Route Optimization & Graph Solver",
                "project_description": "Build an algorithmic routing engine utilizing BFS, Dijkstra, and topological sort for dependency graphs.",
                "days": [
                    {
                        "day_number": 6,
                        "topic": "Binary Trees & Binary Search Trees (BST)",
                        "learning_objective": "Master hierarchical trees, balanced BST invariants, and tree traversals.",
                        "subtopics": ["Pre, In, Post-order traversal", "Lowest common ancestor", "BST validation and search"],
                        "practice_tasks": ["Validate if a binary tree is a valid BST", "Find the maximum depth of a binary tree"],
                        "resources": [
                            {"title": "Binary Tree Algorithms", "url": "https://www.geeksforgeeks.org/binary-tree-data-structure/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    },
                    {
                        "day_number": 7,
                        "topic": "Binary Heaps & Priority Queues",
                        "learning_objective": "Understand min-heaps, max-heaps, heapify operations, and top-K queries.",
                        "subtopics": ["Array representation of heaps", "Heapify algorithm", "Top-K frequent elements"],
                        "practice_tasks": ["Implement a Min-Heap from scratch", "Find Kth Largest Element in an Array"],
                        "resources": [
                            {"title": "Heaps and Priority Queues", "url": "https://en.wikipedia.org/wiki/Binary_heap", "language": "English", "resource_type": "doc", "source": "Wikipedia"}
                        ]
                    },
                    {
                        "day_number": 8,
                        "topic": "Graph Representations, BFS & DFS Traversals",
                        "learning_objective": "Represent graphs via adjacency lists/matrices and traverse connected components.",
                        "subtopics": ["Adjacency lists", "Breadth-First Search for shortest paths", "Depth-First Search for cycle detection"],
                        "practice_tasks": ["Count number of islands in a 2D grid", "Detect cycle in a directed graph"],
                        "resources": [
                            {"title": "Graph Traversals Explained", "url": "https://www.youtube.com/results?search_query=graph+traversal+bfs+dfs", "language": "English", "resource_type": "video", "source": "YouTube"}
                        ]
                    },
                    {
                        "day_number": 9,
                        "topic": "Shortest Path & Topological Sort",
                        "learning_objective": "Implement Dijkstra's shortest path algorithm and Kahn's algorithm for DAG scheduling.",
                        "subtopics": ["Dijkstra's with Priority Queue", "DAG topological sorting", "Course Schedule cycle resolution"],
                        "practice_tasks": ["Solve Course Schedule II (Topological Sort)", "Implement Network Delay Time (Dijkstra)"],
                        "resources": [
                            {"title": "Dijkstra Algorithm Guide", "url": "https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    },
                    {
                        "day_number": 10,
                        "topic": "Dynamic Programming: Memoization & Tabulation",
                        "learning_objective": "Solve optimization problems with overlapping subproblems and optimal substructures.",
                        "subtopics": ["Top-down memoization vs bottom-up tabulation", "0/1 Knapsack problem", "Longest Common Subsequence (LCS)"],
                        "practice_tasks": ["Solve Coin Change (minimum coins needed)", "Implement Longest Increasing Subsequence"],
                        "resources": [
                            {"title": "Dynamic Programming Patterns", "url": "https://leetcode.com/discuss/general-discussion/458695/Dynamic-Programming-Patterns", "language": "English", "resource_type": "article", "source": "LeetCode"}
                        ]
                    }
                ]
            }
        ]
    },
    "python-dev": {
        "id": "python-dev",
        "title": "Python Developer",
        "category": "Programming Languages",
        "badge": "High Demand",
        "description": "Comprehensive journey from Python syntax, OOP, and asynchronous programming to FastAPI microservices, PostgreSQL, and testing.",
        "skills": ["Python 3", "OOP", "FastAPI", "PostgreSQL", "Asyncio", "Pydantic", "Pytest", "Docker"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Python Fundamentals, OOP & Modern Idioms",
                "description": "Type hinting, list comprehensions, object-oriented principles, generators, and decorators.",
                "project_title": "CLI Task & Workflow Management Engine",
                "project_description": "Build an extensible command-line task runner with custom decorators and persistent JSON/SQLite storage.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Python 3 Syntax, Types, Mutability & Collections",
                        "learning_objective": "Understand Python memory model, mutable vs immutable types, dict/set hashing.",
                        "subtopics": ["Lists, Tuples, Dictionaries, Sets", "Comprehensions & Expressions", "Type Hints with typing module"],
                        "practice_tasks": ["Build an in-memory word frequency counter", "Refactor procedural code with typed data structures"],
                        "resources": [
                            {"title": "Official Python 3 Documentation", "url": "https://docs.python.org/3/tutorial/", "language": "English", "resource_type": "doc", "source": "Python.org"},
                            {"title": "Python Full Course in Telugu", "url": "https://www.youtube.com/results?search_query=python+tutorial+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Object-Oriented Programming (OOP) in Python",
                        "learning_objective": "Implement classes, inheritance, dunder methods, and dataclasses.",
                        "subtopics": ["Encapsulation & Properties", "Dunder methods (__repr__, __eq__, __getitem__)", "Python Dataclasses"],
                        "practice_tasks": ["Design an BankAccount system with transaction history and property validation", "Build a custom collection class with __len__ and __iter__"],
                        "resources": [
                            {"title": "Python OOP Deep Dive", "url": "https://realpython.com/python3-object-oriented-programming/", "language": "English", "resource_type": "article", "source": "RealPython"}
                        ]
                    },
                    {
                        "day_number": 3,
                        "topic": "Decorators, Context Managers & Generators",
                        "learning_objective": "Harness functional patterns, write custom decorators, and stream memory-efficient data.",
                        "subtopics": ["Function closures & decorators with *args, **kwargs", "The yield keyword & generator pipelines", "Context managers with __enter__ and __exit__"],
                        "practice_tasks": ["Write an @execution_timer decorator that logs function runtimes", "Build a large file line-streaming generator"],
                        "resources": [
                            {"title": "Primer on Python Decorators", "url": "https://realpython.com/primer-on-python-decorators/", "language": "English", "resource_type": "article", "source": "RealPython"}
                        ]
                    },
                    {
                        "day_number": 4,
                        "topic": "Asynchronous Programming with Asyncio",
                        "learning_objective": "Master the event loop, async/await coroutines, and concurrent I/O task gathering.",
                        "subtopics": ["Event loop architecture", "asyncio.gather vs asyncio.TaskGroup", "Async HTTP requests with httpx"],
                        "practice_tasks": ["Fetch 10 external REST APIs concurrently using httpx.AsyncClient", "Implement an async rate-limited queue worker"],
                        "resources": [
                            {"title": "Async IO in Python: A Complete Walkthrough", "url": "https://realpython.com/async-io-python/", "language": "English", "resource_type": "article", "source": "RealPython"}
                        ]
                    },
                    {
                        "day_number": 5,
                        "topic": "Unit Testing with Pytest & Mocking",
                        "learning_objective": "Write robust automated tests with fixtures, assertions, and mock external dependencies.",
                        "subtopics": ["Pytest test runners", "Fixtures & yield setup/teardown", "unittest.mock for network calls"],
                        "practice_tasks": ["Write a test suite covering an API client with monkeypatch", "Test parametrized edge cases"],
                        "resources": [
                            {"title": "Effective Python Testing with Pytest", "url": "https://realpython.com/pytest-python-testing/", "language": "English", "resource_type": "article", "source": "RealPython"}
                        ]
                    }
                ]
            },
            {
                "phase_number": 2,
                "title": "FastAPI Web Services, Databases & Docker Deployment",
                "description": "Build high-throughput RESTful APIs with Pydantic validation, SQLAlchemy ORM, and Docker containers.",
                "project_title": "Production E-Commerce / Job Board REST API",
                "project_description": "Build a scalable REST API with JWT authentication, PostgreSQL migrations via Alembic, and Docker Compose.",
                "days": [
                    {
                        "day_number": 6,
                        "topic": "FastAPI Architecture, Pydantic & Dependency Injection",
                        "learning_objective": "Structure clean microservices with request parsing, status codes, and Depends() injection.",
                        "subtopics": ["FastAPI routing", "Pydantic BaseModel & Field validation", "Dependency injection for db sessions"],
                        "practice_tasks": ["Create a CRUD endpoint with input sanitization and schema errors", "Implement an API key authentication dependency"],
                        "resources": [
                            {"title": "FastAPI Official Tutorial", "url": "https://fastapi.tiangolo.com/tutorial/", "language": "English", "resource_type": "doc", "source": "FastAPI"}
                        ]
                    },
                    {
                        "day_number": 7,
                        "topic": "PostgreSQL & SQLAlchemy 2.0 ORM Integration",
                        "learning_objective": "Model relational entities, execute joins, and manage database connection pooling.",
                        "subtopics": ["Declarative base models", "Sessionmaker & scoped sessions", "Alembic schema migrations"],
                        "practice_tasks": ["Define 1-to-many relationship (User -> Orders)", "Run an Alembic migration adding indexes"],
                        "resources": [
                            {"title": "SQLAlchemy 2.0 Unified Tutorial", "url": "https://docs.sqlalchemy.org/en/20/tutorial/", "language": "English", "resource_type": "doc", "source": "SQLAlchemy"}
                        ]
                    },
                    {
                        "day_number": 8,
                        "topic": "JWT Authentication & Role-Based Access Control (RBAC)",
                        "learning_objective": "Implement secure password hashing (bcrypt), issue JWT access tokens, and guard routes.",
                        "subtopics": ["Passlib bcrypt hashing", "PyJWT payload signing & expiry", "Current user extraction dependency"],
                        "practice_tasks": ["Build /register and /login with JWT generation", "Protect admin routes using user role check"],
                        "resources": [
                            {"title": "FastAPI Security & JWT Guide", "url": "https://fastapi.tiangolo.com/tutorial/security/oauth2-jwt/", "language": "English", "resource_type": "doc", "source": "FastAPI"}
                        ]
                    },
                    {
                        "day_number": 9,
                        "topic": "Background Tasks & Redis Caching",
                        "learning_objective": "Accelerate read latency with Redis caching and offload long jobs to background tasks.",
                        "subtopics": ["FastAPI BackgroundTasks", "Redis key-value caching", "Cache invalidation strategies"],
                        "practice_tasks": ["Cache top API queries in Redis with a 60s TTL", "Send welcome emails asynchronously in a background task"],
                        "resources": [
                            {"title": "Redis with Python Guide", "url": "https://realpython.com/python-redis/", "language": "English", "resource_type": "article", "source": "RealPython"}
                        ]
                    },
                    {
                        "day_number": 10,
                        "topic": "Docker Containerization & Production Deployment",
                        "learning_objective": "Package applications into multi-stage Docker images and deploy with Uvicorn and Nginx.",
                        "subtopics": ["Dockerfile best practices", "docker-compose for App + DB + Redis", "Gunicorn/Uvicorn production workers"],
                        "practice_tasks": ["Write a multi-stage Dockerfile for FastAPI", "Spin up full stack with docker compose up"],
                        "resources": [
                            {"title": "FastAPI in Containers - Docker", "url": "https://fastapi.tiangolo.com/deployment/docker/", "language": "English", "resource_type": "doc", "source": "Docker"}
                        ]
                    }
                ]
            }
        ]
    },
    "java-dev": {
        "id": "java-dev",
        "title": "Java Developer",
        "category": "Programming Languages",
        "badge": "Enterprise Core",
        "description": "Master Core Java, JVM internals, Collections, Streams, Concurrency, Spring Boot 3, and Microservices.",
        "skills": ["Java 17/21", "Spring Boot", "Hibernate / JPA", "REST APIs", "Microservices", "Maven / Gradle", "PostgreSQL", "Docker"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Core Java, Collections & Streams API",
                "description": "OOP mastery, JVM memory model, Generics, Collections Framework, and functional Streams.",
                "project_title": "High-Throughput In-Memory Banking System",
                "project_description": "Build a thread-safe multi-account banking simulation utilizing Collections, ConcurrentHashMap, and custom Exceptions.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Java Syntax, JVM Architecture, Memory (Heap vs Stack)",
                        "learning_objective": "Understand how bytecode executes, class loaders, Garbage Collection, and Stack vs Heap.",
                        "subtopics": ["JVM, JRE, and JDK internals", "Primitive vs Reference types", "Garbage collection fundamentals"],
                        "practice_tasks": ["Inspect JVM garbage collection logs", "Demonstrate pass-by-value in Java references"],
                        "resources": [
                            {"title": "JVM Architecture Explained", "url": "https://www.geeksforgeeks.org/jvm-works-jvm-architecture/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"},
                            {"title": "Java Full Course in Telugu", "url": "https://www.youtube.com/results?search_query=java+full+course+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "OOP in Java: Polymorphism, Interfaces & Abstract Classes",
                        "learning_objective": "Implement clean contracts, default methods in interfaces, and composition over inheritance.",
                        "subtopics": ["Dynamic method dispatch", "Interface default and static methods", "Sealed classes & Records in Java 17+"],
                        "practice_tasks": ["Create payment gateway abstraction (CreditCard, UPI, PayPal)", "Refactor DTOs to Java Records"],
                        "resources": [
                            {"title": "Java OOP Principles", "url": "https://www.baeldung.com/java-oop", "language": "English", "resource_type": "article", "source": "Baeldung"}
                        ]
                    },
                    {
                        "day_number": 3,
                        "topic": "Java Collections Framework Deep Dive",
                        "learning_objective": "Master internal workings of ArrayList, LinkedList, HashMap, and TreeMap.",
                        "subtopics": ["HashMap collision resolution (Buckets & Red-Black trees)", "Comparable vs Comparator", "Concurrent collections"],
                        "practice_tasks": ["Implement an LRU Cache using LinkedHashMap", "Sort custom objects with multi-field Comparator"],
                        "resources": [
                            {"title": "How HashMap Works Internally", "url": "https://www.geeksforgeeks.org/internal-working-of-hashmap-java/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    },
                    {
                        "day_number": 4,
                        "topic": "Functional Programming & Java Streams API",
                        "learning_objective": "Process data pipelines declaratively using map, filter, flatMap, reduce, and Collectors.",
                        "subtopics": ["Functional interfaces (Predicate, Function, Consumer)", "Intermediate vs Terminal operations", "GroupingBy & partitioningBy collectors"],
                        "practice_tasks": ["Filter and summarize employee salaries by department using Streams", "Flatten nested lists with flatMap"],
                        "resources": [
                            {"title": "The Java 8 Stream API Tutorial", "url": "https://www.baeldung.com/java-8-streams", "language": "English", "resource_type": "article", "source": "Baeldung"}
                        ]
                    },
                    {
                        "day_number": 5,
                        "topic": "Multithreading & Concurrency (ExecutorService, Locks)",
                        "learning_objective": "Manage concurrent execution, avoid race conditions, and use modern thread pools.",
                        "subtopics": ["Thread lifecycle & Runnable", "Synchronized blocks & ReentrantLock", "ExecutorService & CompletableFuture"],
                        "practice_tasks": ["Implement a producer-consumer queue with BlockingQueue", "Execute parallel tasks with CompletableFuture.allOf()"],
                        "resources": [
                            {"title": "Java Concurrency and Multithreading", "url": "https://www.baeldung.com/java-concurrency", "language": "English", "resource_type": "article", "source": "Baeldung"}
                        ]
                    }
                ]
            },
            {
                "phase_number": 2,
                "title": "Spring Boot 3, Spring Data JPA & Microservices",
                "description": "Enterprise API development, Dependency Injection, Hibernate entity mapping, and Docker.",
                "project_title": "Enterprise Cloud Order Management System",
                "project_description": "Production-grade Spring Boot REST API with Spring Data JPA, PostgreSQL, Flyway migrations, and Swagger UI.",
                "days": [
                    {
                        "day_number": 6,
                        "topic": "Spring Boot 3 Core, IoC & Dependency Injection",
                        "learning_objective": "Understand Spring ApplicationContext, @Component, @Service, and @Autowired beans.",
                        "subtopics": ["Inversion of Control (IoC)", "Constructor injection vs Field injection", "Application properties & profiles"],
                        "practice_tasks": ["Configure multi-environment profiles (dev, prod)", "Create a decoupled notification service with DI"],
                        "resources": [
                            {"title": "Building a RESTful Web Service with Spring", "url": "https://spring.io/guides/gs/rest-service/", "language": "English", "resource_type": "doc", "source": "Spring.io"}
                        ]
                    },
                    {
                        "day_number": 7,
                        "topic": "Spring Data JPA & Hibernate Relational Mapping",
                        "learning_objective": "Define JPA entities, manage transactions, write derived query methods, and optimize queries.",
                        "subtopics": ["@Entity, @Id, @GeneratedValue", "Relationships (@OneToMany, @ManyToOne)", "Solving N+1 query problem with @EntityGraph"],
                        "practice_tasks": ["Build bidirectional User-Order entity mapping", "Write custom JPQL queries with joins"],
                        "resources": [
                            {"title": "Spring Data JPA Tutorial", "url": "https://www.baeldung.com/the-persistence-layer-with-spring-data-jpa", "language": "English", "resource_type": "article", "source": "Baeldung"}
                        ]
                    },
                    {
                        "day_number": 8,
                        "topic": "Spring Security 6 & JWT Token Authentication",
                        "learning_objective": "Secure REST endpoints with stateless JWT authentication and role-based filters.",
                        "subtopics": ["SecurityFilterChain configuration", "UsernamePasswordAuthenticationFilter", "JWT token generation and validation"],
                        "practice_tasks": ["Create secure login and token refresh endpoints", "Protect endpoints with @PreAuthorize('hasRole(\"ADMIN\")')"],
                        "resources": [
                            {"title": "Spring Security with JWT", "url": "https://www.baeldung.com/spring-security-jwt", "language": "English", "resource_type": "article", "source": "Baeldung"}
                        ]
                    },
                    {
                        "day_number": 9,
                        "topic": "Exception Handling, Validation & OpenAPI / Swagger Docs",
                        "learning_objective": "Enforce clean API contracts with Bean Validation (@Valid) and centralized @ControllerAdvice.",
                        "subtopics": ["Global exception handling with @ExceptionHandler", "Hibernate validator annotations", "springdoc-openapi Swagger UI"],
                        "practice_tasks": ["Create uniform ErrorResponse DTO with timestamp and field errors", "Generate interactive Swagger UI docs"],
                        "resources": [
                            {"title": "Spring Boot REST Exception Handling", "url": "https://www.baeldung.com/exception-handling-for-rest-with-spring", "language": "English", "resource_type": "article", "source": "Baeldung"}
                        ]
                    },
                    {
                        "day_number": 10,
                        "topic": "Microservices Communication & Cloud Deployment",
                        "learning_objective": "Connect microservices using OpenFeign / WebClient and build containerized JARs.",
                        "subtopics": ["REST client with OpenFeign", "Dockerizing Spring Boot with layered JARs", "Service discovery & API Gateway basics"],
                        "practice_tasks": ["Package app into a lightweight Docker image using eclipse-temurin", "Communicate with external service via WebClient"],
                        "resources": [
                            {"title": "Spring Microservices Architecture", "url": "https://spring.io/microservices", "language": "English", "resource_type": "doc", "source": "Spring.io"}
                        ]
                    }
                ]
            }
        ]
    },
    "ml-ai-dev": {
        "id": "ml-ai-dev",
        "title": "Machine Learning & AI Developer",
        "category": "AI & Data Science",
        "badge": "Cutting Edge",
        "description": "Mathematics for ML, NumPy, Pandas, Scikit-Learn, Deep Learning, PyTorch, LLMs, and Model Deployment.",
        "skills": ["Machine Learning", "Deep Learning", "PyTorch", "NumPy & Pandas", "Scikit-Learn", "NLP", "LLMs", "HuggingFace"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Data Engineering, NumPy, Pandas & Classic ML",
                "description": "Exploratory data analysis, feature engineering, classification, regression, and cross-validation.",
                "project_title": "End-to-End Customer Churn & Salary Prediction Pipeline",
                "project_description": "Clean raw tabular datasets, engineer features, evaluate Random Forest & XGBoost models with ROC-AUC scoring.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "NumPy Arrays, Vectorization & Linear Algebra Foundations",
                        "learning_objective": "Execute vectorized mathematical operations, matrix multiplications, and broadcasting.",
                        "subtopics": ["Broadcasting rules", "Dot products & matrix factorization", "Performance vs Python loops"],
                        "practice_tasks": ["Compute cosine similarity matrix between vector embeddings", "Vectorize Euclidean distance calculations without loops"],
                        "resources": [
                            {"title": "NumPy Quickstart Tutorial", "url": "https://numpy.org/doc/stable/user/quickstart.html", "language": "English", "resource_type": "doc", "source": "NumPy"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Pandas for Data Manipulation & Cleaning",
                        "learning_objective": "Load, slice, aggregate, and impute real-world tabular data.",
                        "subtopics": ["DataFrames and Series", "Handling missing values and outliers", "GroupBy and pivot tables"],
                        "practice_tasks": ["Clean a raw CSV with missing values and categorical data", "Perform exploratory multi-column aggregations"],
                        "resources": [
                            {"title": "10 Minutes to Pandas", "url": "https://pandas.pydata.org/docs/user_guide/10min.html", "language": "English", "resource_type": "doc", "source": "Pandas"}
                        ]
                    },
                    {
                        "day_number": 3,
                        "topic": "Supervised Learning: Linear & Logistic Regression",
                        "learning_objective": "Understand loss functions (MSE, Binary Cross-Entropy), gradient descent, and decision boundaries.",
                        "subtopics": ["Gradient descent optimization", "Regularization (L1 Lasso, L2 Ridge)", "Sigmoid activation and odds ratios"],
                        "practice_tasks": ["Train a Ridge regression model with cross-validation", "Build a binary classification model with scikit-learn"],
                        "resources": [
                            {"title": "Scikit-Learn Classification Guide", "url": "https://scikit-learn.org/stable/supervised_learning.html", "language": "English", "resource_type": "doc", "source": "Scikit-Learn"}
                        ]
                    },
                    {
                        "day_number": 4,
                        "topic": "Tree-Based Models: Decision Trees, Random Forests, XGBoost",
                        "learning_objective": "Harness ensemble techniques, bagging, boosting, and hyperparameter tuning.",
                        "subtopics": ["Gini impurity & Information Gain", "Random Forest bagging", "Gradient Boosting (XGBoost/LightGBM)"],
                        "practice_tasks": ["Tune an XGBoost classifier with GridSearchCV", "Extract and plot feature importances"],
                        "resources": [
                            {"title": "Ensemble Methods Guide", "url": "https://scikit-learn.org/stable/modules/ensemble.html", "language": "English", "resource_type": "doc", "source": "Scikit-Learn"}
                        ]
                    },
                    {
                        "day_number": 5,
                        "topic": "Model Evaluation Metrics & Unsupervised Clustering",
                        "learning_objective": "Diagnose precision, recall, F1-score, ROC-AUC, and execute K-Means clustering.",
                        "subtopics": ["Confusion matrix analysis", "Precision-Recall trade-off", "K-Means & PCA dimensionality reduction"],
                        "practice_tasks": ["Plot ROC-AUC curves for multiple models", "Cluster customer segments with K-Means and visualize with PCA"],
                        "resources": [
                            {"title": "Model Evaluation Metrics Explained", "url": "https://scikit-learn.org/stable/modules/model_evaluation.html", "language": "English", "resource_type": "doc", "source": "Scikit-Learn"}
                        ]
                    }
                ]
            },
            {
                "phase_number": 2,
                "title": "Deep Learning, PyTorch, NLP & Large Language Models",
                "description": "Neural network architectures, backpropagation, transformers, HuggingFace embeddings, and RAG pipelines.",
                "project_title": "AI Semantic Resume Q&A and RAG Knowledge Engine",
                "project_description": "Build a Retrieval-Augmented Generation (RAG) system using vector embeddings, FAISS, and LangChain/LlamaIndex.",
                "days": [
                    {
                        "day_number": 6,
                        "topic": "Neural Networks Foundations & PyTorch Tensors",
                        "learning_objective": "Build multi-layer perceptrons, forward passes, autograd, and backpropagation in PyTorch.",
                        "subtopics": ["PyTorch tensors & GPU acceleration", "Activation functions (ReLU, Softmax)", "Cross-Entropy loss & Adam optimizer"],
                        "practice_tasks": ["Train a 3-layer neural network on MNIST dataset", "Inspect gradients using tensor.grad"],
                        "resources": [
                            {"title": "PyTorch 60 Minute Blitz", "url": "https://pytorch.org/tutorials/beginner/deep_learning_60min_blitz.html", "language": "English", "resource_type": "doc", "source": "PyTorch"}
                        ]
                    },
                    {
                        "day_number": 7,
                        "topic": "Convolutional Neural Networks (CNNs) & Computer Vision",
                        "learning_objective": "Understand convolution kernels, pooling layers, and transfer learning with ResNet.",
                        "subtopics": ["Conv2d layers & feature maps", "Max pooling & batch normalization", "Fine-tuning pre-trained TorchVision models"],
                        "practice_tasks": ["Train an image classifier with transfer learning (ResNet18)", "Apply data augmentation transforms"],
                        "resources": [
                            {"title": "Transfer Learning in PyTorch", "url": "https://pytorch.org/tutorials/beginner/transfer_learning_tutorial.html", "language": "English", "resource_type": "doc", "source": "PyTorch"}
                        ]
                    },
                    {
                        "day_number": 8,
                        "topic": "Natural Language Processing (NLP) & Word Embeddings",
                        "learning_objective": "Tokenize text, generate semantic vector embeddings, and compute cosine similarities.",
                        "subtopics": ["Tokenization & TF-IDF", "Word2Vec & Sentence Transformers", "Vector indexing with FAISS / ChromaDB"],
                        "practice_tasks": ["Generate semantic embeddings for 100 sentences with sentence-transformers", "Index embeddings in FAISS and execute top-3 nearest neighbor queries"],
                        "resources": [
                            {"title": "Sentence Transformers Documentation", "url": "https://www.sbert.net/", "language": "English", "resource_type": "doc", "source": "SBERT"}
                        ]
                    },
                    {
                        "day_number": 9,
                        "topic": "The Transformer Architecture & Self-Attention",
                        "learning_objective": "Deconstruct self-attention mechanisms, multi-head attention, encoders, and decoders.",
                        "subtopics": ["Scaled dot-product attention", "Positional encodings", "BERT vs GPT architectural differences"],
                        "practice_tasks": ["Implement a toy self-attention module in PyTorch", "Run text classification using a HuggingFace pipeline"],
                        "resources": [
                            {"title": "The Illustrated Transformer by Jay Alammar", "url": "https://jalammar.github.io/illustrated-transformer/", "language": "English", "resource_type": "article", "source": "Jay Alammar"}
                        ]
                    },
                    {
                        "day_number": 10,
                        "topic": "Building RAG Applications & Model Inference API",
                        "learning_objective": "Construct end-to-end Retrieval-Augmented Generation (RAG) and serve models via FastAPI.",
                        "subtopics": ["Document chunking strategies", "Prompt template engineering", "FastAPI inference endpoint with streaming"],
                        "practice_tasks": ["Build a working RAG pipeline over local PDF documents", "Deploy model inference behind an async FastAPI endpoint"],
                        "resources": [
                            {"title": "Retrieval Augmented Generation (RAG) Guide", "url": "https://www.promptingguide.ai/techniques/rag", "language": "English", "resource_type": "article", "source": "PromptingGuide"}
                        ]
                    }
                ]
            }
        ]
    },
    "fullstack-web": {
        "id": "fullstack-web",
        "title": "Full Stack Web Developer",
        "category": "Web & Mobile",
        "badge": "Popular",
        "description": "Modern frontend with React & Tailwind, backend REST/GraphQL with Node.js & Express, and PostgreSQL/MongoDB integration.",
        "skills": ["JavaScript", "TypeScript", "React", "Node.js", "Express", "PostgreSQL", "Tailwind CSS", "REST APIs"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Modern JavaScript (ES6+), React & Responsive UI",
                "description": "State management, React hooks, component lifecycle, virtual DOM, and Tailwind styling.",
                "project_title": "Interactive Job Finder & Analytics Dashboard",
                "project_description": "Build a responsive React application featuring real-time filters, search, and dynamic statistics charts.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Modern JavaScript (ES6+): Closures, Promises & Async/Await",
                        "learning_objective": "Master the JavaScript event loop, asynchronous execution, and destructuring.",
                        "subtopics": ["Arrow functions & Lexical this", "Promises & async/await error handling", "Array methods: map, filter, reduce"],
                        "practice_tasks": ["Build an async fetch utility with retries", "Refactor callback hell into clean Promises"],
                        "resources": [
                            {"title": "JavaScript.info - The Modern JavaScript Tutorial", "url": "https://javascript.info/", "language": "English", "resource_type": "doc", "source": "JavaScript.info"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "React Core: JSX, Components, Props & State",
                        "learning_objective": "Understand declarative UI, unidirectional data flow, and the Virtual DOM.",
                        "subtopics": ["Functional components & JSX syntax", "Props vs State", "Conditional rendering & lists"],
                        "practice_tasks": ["Build a dynamic Todo/Task component with completed toggles", "Create reusable Card and Button components"],
                        "resources": [
                            {"title": "React Official Documentation", "url": "https://react.dev/learn", "language": "English", "resource_type": "doc", "source": "React.dev"}
                        ]
                    },
                    {
                        "day_number": 3,
                        "topic": "React Hooks: useState, useEffect, useRef, useMemo",
                        "learning_objective": "Manage component state, side effects, API fetching, and performance caching.",
                        "subtopics": ["useState state updater functions", "useEffect dependency array rules", "useMemo & useCallback for optimization"],
                        "practice_tasks": ["Fetch API data with loading and error states using useEffect", "Implement search debounce with useEffect and setTimeout"],
                        "resources": [
                            {"title": "A Complete Guide to useEffect", "url": "https://overreacted.io/a-complete-guide-to-useeffect/", "language": "English", "resource_type": "article", "source": "Dan Abramov"}
                        ]
                    },
                    {
                        "day_number": 4,
                        "topic": "Component Styling with Tailwind CSS & Responsive Design",
                        "learning_objective": "Build modern layouts using utility classes, Flexbox, CSS Grid, and mobile breakpoints.",
                        "subtopics": ["Tailwind utility classes", "Mobile-first responsive modifiers (sm, md, lg)", "Dark mode styling with Tailwind"],
                        "practice_tasks": ["Code a responsive navbar with mobile hamburger menu", "Style a 3-column pricing grid with hover animations"],
                        "resources": [
                            {"title": "Tailwind CSS Documentation", "url": "https://tailwindcss.com/docs", "language": "English", "resource_type": "doc", "source": "TailwindCSS"}
                        ]
                    },
                    {
                        "day_number": 5,
                        "topic": "Client-Side Routing & Global State (React Router & Zustand)",
                        "learning_objective": "Implement multi-page client navigation and lightweight global state stores.",
                        "subtopics": ["React Router DOM (BrowserRouter, Routes, Route)", "URL params and query string extraction", "Zustand state store setup"],
                        "practice_tasks": ["Configure multi-route app with protected authentication routes", "Create a global shopping cart / bookmark store using Zustand"],
                        "resources": [
                            {"title": "Zustand State Management", "url": "https://github.com/pmndrs/zustand", "language": "English", "resource_type": "doc", "source": "GitHub"}
                        ]
                    }
                ]
            },
            {
                "phase_number": 2,
                "title": "Backend Engineering: Node.js, Express & Database Architecture",
                "description": "RESTful endpoints, middleware, authentication with JWT, and MongoDB / PostgreSQL queries.",
                "project_title": "Full Stack SaaS Platform with User Accounts & CRUD",
                "project_description": "Complete full-stack application connecting React frontend to Node.js/Express API with database persistence.",
                "days": [
                    {
                        "day_number": 6,
                        "topic": "Node.js Runtime & Express Server Setup",
                        "learning_objective": "Build HTTP servers, handle requests and responses, and structure project directories.",
                        "subtopics": ["CommonJS vs ES Modules in Node", "Express app initialization & route handlers", "Parsing JSON request bodies"],
                        "practice_tasks": ["Create Express server with GET, POST, PUT, DELETE endpoints", "Implement request logging middleware"],
                        "resources": [
                            {"title": "Express.js Getting Started", "url": "https://expressjs.com/en/starter/installing.html", "language": "English", "resource_type": "doc", "source": "ExpressJS"}
                        ]
                    },
                    {
                        "day_number": 7,
                        "topic": "Express Middleware, Validation & Error Handling",
                        "learning_objective": "Centralize error handling, validate input payloads with Zod, and enforce CORS.",
                        "subtopics": ["Custom middleware pipeline", "Payload validation with Zod", "Centralized 4-argument error handler"],
                        "practice_tasks": ["Write validation middleware that returns 400 on invalid input", "Set up CORS whitelist for frontend origin"],
                        "resources": [
                            {"title": "Writing Express Middleware", "url": "https://expressjs.com/en/guide/writing-middleware.html", "language": "English", "resource_type": "doc", "source": "ExpressJS"}
                        ]
                    },
                    {
                        "day_number": 8,
                        "topic": "Database Persistence: PostgreSQL with Prisma ORM",
                        "learning_objective": "Define relational schemas, execute migrations, and perform type-safe database queries.",
                        "subtopics": ["Prisma schema definition", "Prisma Client CRUD queries", "Handling foreign keys & cascading deletes"],
                        "practice_tasks": ["Define User and Post models with Prisma", "Seed database with 50 sample records"],
                        "resources": [
                            {"title": "Prisma ORM Getting Started", "url": "https://www.prisma.io/docs/getting-started", "language": "English", "resource_type": "doc", "source": "Prisma"}
                        ]
                    },
                    {
                        "day_number": 9,
                        "topic": "Authentication & Authorization (Bcrypt & JWT)",
                        "learning_objective": "Store salted password hashes, issue signed tokens, and guard API endpoints.",
                        "subtopics": ["bcryptjs hashing & compare", "jsonwebtoken signing and verification", "authMiddleware route protection"],
                        "practice_tasks": ["Implement user registration and login endpoints", "Protect private resources so only the creator can edit"],
                        "resources": [
                            {"title": "JWT Authentication in Node.js", "url": "https://www.digitalocean.com/community/tutorials/nodejs-jwt-expressjs", "language": "English", "resource_type": "article", "source": "DigitalOcean"}
                        ]
                    },
                    {
                        "day_number": 10,
                        "topic": "Full-Stack Deployment & CI/CD",
                        "learning_objective": "Deploy backend to Render/Fly.io, frontend to Vercel/Netlify, and configure environment variables.",
                        "subtopics": ["Environment variables (.env and production secrets)", "Connecting frontend to remote backend URL", "Build scripts and healthcheck endpoints"],
                        "practice_tasks": ["Configure Vite build and proxy settings", "Deploy live application with working remote database"],
                        "resources": [
                            {"title": "Deploying Full Stack Apps to Render", "url": "https://render.com/docs", "language": "English", "resource_type": "doc", "source": "Render"}
                        ]
                    }
                ]
            }
        ]
    },
    "os-cse": {
        "id": "os-cse",
        "title": "Operating Systems & Concurrency",
        "category": "Core CSE",
        "badge": "Core Subject",
        "description": "Kernel architecture, process scheduling, multithreading, synchronization primitives, memory management, and file systems.",
        "skills": ["Operating Systems", "Process Management", "Concurrency", "CPU Scheduling", "Virtual Memory", "Deadlocks"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Processes, Threads, CPU Scheduling & Synchronization",
                "description": "Process control blocks, context switching, scheduling algorithms, race conditions, and semaphores.",
                "project_title": "Multi-Algorithm CPU Scheduler & Thread Pool Simulator",
                "project_description": "Implement FCFS, Round Robin, and SJF scheduling algorithms comparing turnaround and wait times.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "OS Kernel, System Calls & Process Lifecycle",
                        "learning_objective": "Differentiate User Mode vs Kernel Mode, System Calls, and Process Control Blocks (PCB).",
                        "subtopics": ["Dual-mode CPU operation (Ring 0 vs Ring 3)", "System call interrupts", "Process states: New, Ready, Running, Waiting, Terminated"],
                        "practice_tasks": ["Diagram PCB transitions during an I/O wait", "Explain fork(), exec(), and wait() system calls"],
                        "resources": [
                            {"title": "Operating Systems: Three Easy Pieces", "url": "https://pages.cs.wisc.edu/~remzi/OSTEP/", "language": "English", "resource_type": "doc", "source": "OSTEP"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "CPU Scheduling Algorithms (FCFS, SJF, Round Robin)",
                        "learning_objective": "Calculate waiting times and turnaround times across preemptive and non-preemptive algorithms.",
                        "subtopics": ["First-Come First-Served (FCFS)", "Shortest Job First (SJF) & Shortest Remaining Time First", "Round Robin with time quantum selection"],
                        "practice_tasks": ["Calculate average wait time for 4 processes with given burst times", "Simulate Convoy effect in FCFS"],
                        "resources": [
                            {"title": "CPU Scheduling in Operating Systems", "url": "https://www.geeksforgeeks.org/cpu-scheduling-in-operating-systems/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    },
                    {
                        "day_number": 3,
                        "topic": "Process Synchronization, Critical Section & Mutexes",
                        "learning_objective": "Solve the Critical Section problem ensuring Mutual Exclusion, Progress, and Bounded Waiting.",
                        "subtopics": ["Race conditions and shared memory", "Peterson's Solution", "Mutex locks vs Counting Semaphores"],
                        "practice_tasks": ["Write pseudocode for producer-consumer using semaphores", "Demonstrate race condition without synchronization"],
                        "resources": [
                            {"title": "Process Synchronization", "url": "https://www.geeksforgeeks.org/introduction-of-process-synchronization/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    },
                    {
                        "day_number": 4,
                        "topic": "Classical Concurrency: Dining Philosophers & Readers-Writers",
                        "learning_objective": "Analyze and solve classic synchronization challenges without deadlocks or starvation.",
                        "subtopics": ["Dining Philosophers problem", "Readers-Writers problem with write priority", "Bounded-buffer problem"],
                        "practice_tasks": ["Provide deadlock-free solution for Dining Philosophers", "Implement reader-writer lock"],
                        "resources": [
                            {"title": "Classical Problems of Synchronization", "url": "https://www.geeksforgeeks.org/classical-problems-of-synchronization-with-semaphore-solution/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    },
                    {
                        "day_number": 5,
                        "topic": "Deadlocks: 4 Conditions, Detection & Banker's Algorithm",
                        "learning_objective": "Understand Mutual Exclusion, Hold & Wait, No Preemption, Circular Wait, and avoidance strategies.",
                        "subtopics": ["Deadlock characterization", "Resource Allocation Graphs (RAG)", "Banker's Algorithm for safe state detection"],
                        "practice_tasks": ["Trace Banker's Algorithm to find if system is in safe state", "Draw Resource Allocation Graph showing deadlock"],
                        "resources": [
                            {"title": "Banker's Algorithm in Operating Systems", "url": "https://www.geeksforgeeks.org/bankers-algorithm-in-operating-system-2/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    }
                ]
            },
            {
                "phase_number": 2,
                "title": "Memory Management, Virtual Memory & File Systems",
                "description": "Paging, segmentation, TLB caches, page replacement algorithms (FIFO, LRU), and inode structures.",
                "project_title": "Virtual Memory Manager & Page Replacement Simulator",
                "project_description": "Build a simulation of page tables, TLB hits/misses, and evaluate LRU vs FIFO page faults.",
                "days": [
                    {
                        "day_number": 6,
                        "topic": "Memory Management: Paging, Segmentation & MMU",
                        "learning_objective": "Understand logical vs physical addresses, page tables, and memory protection.",
                        "subtopics": ["Memory Management Unit (MMU)", "Page tables and frame allocation", "Internal vs External fragmentation"],
                        "practice_tasks": ["Translate a 32-bit logical address into physical frame address", "Calculate page table size for a given virtual memory configuration"],
                        "resources": [
                            {"title": "Paging in Operating Systems", "url": "https://www.geeksforgeeks.org/paging-in-operating-system/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    },
                    {
                        "day_number": 7,
                        "topic": "Translation Lookaside Buffer (TLB) & Multi-level Paging",
                        "learning_objective": "Analyze TLB hit ratios, effective memory access time, and hierarchical page tables.",
                        "subtopics": ["TLB cache architecture", "Effective Memory Access Time (EMAT) formula", "Two-level and inverted page tables"],
                        "practice_tasks": ["Compute EMAT with 95% TLB hit ratio and 20ns TLB lookup", "Design a 2-level page table hierarchy"],
                        "resources": [
                            {"title": "Translation Lookaside Buffer (TLB)", "url": "https://www.geeksforgeeks.org/translation-lookaside-buffer-tlb-in-paging/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    },
                    {
                        "day_number": 8,
                        "topic": "Virtual Memory & Page Replacement Algorithms (LRU, FIFO, Optimal)",
                        "learning_objective": "Understand demand paging, page faults, Belady's Anomaly, and LRU eviction.",
                        "subtopics": ["Demand paging and page fault handler", "FIFO page replacement & Belady's Anomaly", "Least Recently Used (LRU) & Clock algorithm"],
                        "practice_tasks": ["Calculate page faults for reference string under FIFO, LRU, and Optimal", "Implement Clock / Second-Chance algorithm"],
                        "resources": [
                            {"title": "Page Replacement Algorithms in OS", "url": "https://www.geeksforgeeks.org/page-replacement-algorithms-in-operating-systems/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    },
                    {
                        "day_number": 9,
                        "topic": "File Systems Architecture & Inode Structures",
                        "learning_objective": "Understand how files, directories, and inodes are organized on physical storage media.",
                        "subtopics": ["File attributes and operations", "Unix Inode structure (Direct, Single/Double Indirect pointers)", "Contiguous, Linked, and Indexed allocation"],
                        "practice_tasks": ["Calculate maximum file size supported by an inode with 12 direct pointers", "Compare FAT32 vs Ext4 filesystem architecture"],
                        "resources": [
                            {"title": "File Systems - OSTEP", "url": "https://pages.cs.wisc.edu/~remzi/OSTEP/file-intro.pdf", "language": "English", "resource_type": "doc", "source": "OSTEP"}
                        ]
                    },
                    {
                        "day_number": 10,
                        "topic": "Disk Scheduling (SSTF, SCAN, C-SCAN) & I/O Systems",
                        "learning_objective": "Calculate head movement tracks across disk scheduling algorithms and understand I/O DMA.",
                        "subtopics": ["Disk geometry: Cylinders, Tracks, Sectors", "SCAN / Elevator algorithm & C-SCAN", "Direct Memory Access (DMA) vs Interrupts"],
                        "practice_tasks": ["Compute total head movement for given cylinder requests under SCAN", "Explain DMA controller operation during disk read"],
                        "resources": [
                            {"title": "Disk Scheduling Algorithms", "url": "https://www.geeksforgeeks.org/disk-scheduling-algorithms/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    }
                ]
            }
        ]
    },
    "dbms-cse": {
        "id": "dbms-cse",
        "title": "Database Management Systems (DBMS & SQL)",
        "category": "Core CSE",
        "badge": "Core Subject",
        "description": "Relational algebra, SQL mastery, indexing (B+ Trees), Normalization (1NF to BCNF), transactions (ACID), and concurrency.",
        "skills": ["SQL", "Relational Database", "B+ Trees", "Normalization", "Transactions", "ACID", "PostgreSQL", "NoSQL"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Relational Model, Advanced SQL & Database Schema Design",
                "description": "ER modeling, complex SQL joins, aggregations, window functions, and subqueries.",
                "project_title": "Enterprise Relational Database Schema & Analytics Engine",
                "project_description": "Design an ER diagram and write advanced analytical SQL queries with window functions for financial reports.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Relational Data Model & Entity-Relationship (ER) Diagrams",
                        "learning_objective": "Map business domains into entities, attributes, primary keys, foreign keys, and cardinalities.",
                        "subtopics": ["Strong vs Weak entities", "Cardinality ratios (1:1, 1:N, M:N)", "Relational algebra operators (Select, Project, Join)"],
                        "practice_tasks": ["Design ER diagram for an Airline Reservation system", "Translate ER schema into SQL CREATE TABLE statements with foreign keys"],
                        "resources": [
                            {"title": "Database System Concepts (Silberschatz)", "url": "https://www.db-book.com/", "language": "English", "resource_type": "doc", "source": "Silberschatz"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "SQL Mastery: Complex Joins, Group By & Having",
                        "learning_objective": "Write performant queries using INNER, LEFT, RIGHT, FULL OUTER joins and aggregates.",
                        "subtopics": ["Join mechanics and nullability", "WHERE vs HAVING filters", "Self-joins and cross-joins"],
                        "practice_tasks": ["Find customers who placed orders in 2025 but none in 2026", "Aggregate department expenditure with HAVING count > 5"],
                        "resources": [
                            {"title": "SQL Tutorial - W3Schools", "url": "https://www.w3schools.com/sql/", "language": "English", "resource_type": "doc", "source": "W3Schools"}
                        ]
                    },
                    {
                        "day_number": 3,
                        "topic": "Advanced SQL: Window Functions & Common Table Expressions (CTEs)",
                        "learning_objective": "Leverage ROW_NUMBER(), RANK(), DENSE_RANK(), LEAD(), LAG(), and recursive CTEs.",
                        "subtopics": ["OVER (PARTITION BY ... ORDER BY ...)", "Running totals and moving averages", "WITH RECURSIVE for hierarchical tree queries"],
                        "practice_tasks": ["Find top 3 highest-earning employees per department using DENSE_RANK", "Calculate month-over-month revenue growth using LAG()"],
                        "resources": [
                            {"title": "PostgreSQL Window Functions Tutorial", "url": "https://www.postgresqltutorial.com/postgresql-window-function/", "language": "English", "resource_type": "article", "source": "PostgreSQL Tutorial"}
                        ]
                    },
                    {
                        "day_number": 4,
                        "topic": "Database Normalization (1NF, 2NF, 3NF, BCNF)",
                        "learning_objective": "Eliminate data redundancy and update anomalies using functional dependencies.",
                        "subtopics": ["Insertion, Deletion, and Update anomalies", "1st, 2nd, and 3rd Normal Form definitions", "Boyce-Codd Normal Form (BCNF) requirements"],
                        "practice_tasks": ["Decompose an unnormalized table into 3NF", "Identify candidate keys from a set of functional dependencies"],
                        "resources": [
                            {"title": "Database Normalization Explained", "url": "https://www.geeksforgeeks.org/introduction-of-database-normalization/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    },
                    {
                        "day_number": 5,
                        "topic": "Transactions, ACID Properties & Isolation Levels",
                        "learning_objective": "Understand Atomicity, Consistency, Isolation, Durability, and transaction isolation anomalies.",
                        "subtopics": ["Dirty Reads, Non-repeatable Reads, Phantom Reads", "Read Committed vs Repeatable Read vs Serializable", "Two-Phase Locking (2PL)"],
                        "practice_tasks": ["Demonstrate non-repeatable read scenario with concurrent transactions", "Write a transactional money transfer with error rollback"],
                        "resources": [
                            {"title": "ACID Properties in DBMS", "url": "https://www.geeksforgeeks.org/acid-properties-in-dbms/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    }
                ]
            },
            {
                "phase_number": 2,
                "title": "Indexing, B+ Trees, Query Optimization & NoSQL",
                "description": "Database indexing, B+ Tree internals, execution plans, query optimization, and MongoDB/Redis.",
                "project_title": "Database Performance Tuning & Indexing Benchmark",
                "project_description": "Analyze EXPLAIN ANALYZE execution plans, optimize slow queries with composite indexes, and benchmark response times.",
                "days": [
                    {
                        "day_number": 6,
                        "topic": "B+ Tree Indexing & Hash Index Internals",
                        "learning_objective": "Understand why databases use B+ trees, fanout, disk I/O reduction, and index clustering.",
                        "subtopics": ["B-Tree vs B+ Tree differences", "Clustered vs Non-clustered indexes", "Composite indexes and leftmost prefix rule"],
                        "practice_tasks": ["Trace B+ tree search path for a range query", "Design optimal index for a query with equality and range filters"],
                        "resources": [
                            {"title": "Use The Index, Luke! - SQL Indexing Guide", "url": "https://use-the-index-luke.com/", "language": "English", "resource_type": "doc", "source": "Markus Winand"}
                        ]
                    },
                    {
                        "day_number": 7,
                        "topic": "Query Execution Plans & EXPLAIN ANALYZE",
                        "learning_objective": "Read execution plans, identify sequential scans, index scans, and join strategies.",
                        "subtopics": ["Seq Scan vs Index Scan vs Bitmap Index Scan", "Nested Loop vs Hash Join vs Merge Join", "PostgreSQL EXPLAIN ANALYZE interpretation"],
                        "practice_tasks": ["Optimize a slow 5-second query down to < 20ms using an index", "Identify why database chose a sequential scan over an index"],
                        "resources": [
                            {"title": "Understanding EXPLAIN in PostgreSQL", "url": "https://www.depesz.com/tag/explain/", "language": "English", "resource_type": "article", "source": "Depesz"}
                        ]
                    },
                    {
                        "day_number": 8,
                        "topic": "Concurrency Control: Two-Phase Locking & MVCC",
                        "learning_objective": "Master Multiversion Concurrency Control (MVCC), deadlocks, and write locks.",
                        "subtopics": ["Strict 2PL and Rigorous 2PL", "MVCC implementation in PostgreSQL (xmin, xmax)", "Optimistic vs Pessimistic concurrency control"],
                        "practice_tasks": ["Explain how MVCC allows readers to never block writers", "Detect and break a database deadlock in SQL"],
                        "resources": [
                            {"title": "PostgreSQL MVCC Internals", "url": "https://devcenter.heroku.com/articles/postgresql-concurrency", "language": "English", "resource_type": "article", "source": "Heroku Dev"}
                        ]
                    },
                    {
                        "day_number": 9,
                        "topic": "Database Replication, Partitioning & Sharding",
                        "learning_objective": "Scale databases horizontally, configure read replicas, and partition massive tables.",
                        "subtopics": ["Primary-Replica replication", "Range vs Hash table partitioning", "Horizontal sharding and partition keys"],
                        "practice_tasks": ["Configure declarative table partitioning by date range in PostgreSQL", "Explain consistent hashing in distributed databases"],
                        "resources": [
                            {"title": "Table Partitioning in PostgreSQL", "url": "https://www.postgresql.org/docs/current/ddl-partitioning.html", "language": "English", "resource_type": "doc", "source": "PostgreSQL.org"}
                        ]
                    },
                    {
                        "day_number": 10,
                        "topic": "NoSQL Paradigms: Document (MongoDB) & Key-Value (Redis)",
                        "learning_objective": "Compare SQL vs NoSQL trade-offs, CAP theorem, and when to use MongoDB or Redis.",
                        "subtopics": ["CAP theorem (Consistency, Availability, Partition Tolerance)", "MongoDB JSON document schema & indexing", "Redis caching data structures (Strings, Hashes, Sets)"],
                        "practice_tasks": ["Write MongoDB aggregation pipeline for e-commerce analytics", "Implement distributed rate limiter using Redis INCR with TTL"],
                        "resources": [
                            {"title": "MongoDB University Free Courses", "url": "https://learn.mongodb.com/", "language": "English", "resource_type": "doc", "source": "MongoDB"}
                        ]
                    }
                ]
            }
        ]
    },
    "cn-cse": {
        "id": "cn-cse",
        "title": "Computer Networks & Protocols",
        "category": "Core CSE",
        "badge": "Core Subject",
        "description": "OSI 7 Layers, TCP/IP, IP addressing (IPv4/IPv6), subnetting, routing protocols, HTTP/HTTPS, DNS, and network security.",
        "skills": ["Computer Networks", "TCP/IP", "HTTP/HTTPS", "DNS", "Subnetting", "Routing Protocols", "Sockets", "TLS/SSL"],
        "phases": [
            {
                "phase_number": 1,
                "title": "OSI Reference Model, IP Addressing & Transport Layer (TCP/UDP)",
                "description": "Layer responsibilities, IPv4 CIDR subnetting, TCP 3-way handshake, flow control, and congestion control.",
                "project_title": "Network Packet Sniffer & Subnetting Calculator",
                "project_description": "Build an interactive IPv4 CIDR subnet calculator and a Python socket packet analyzer.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "The 7-Layer OSI Model & TCP/IP Protocol Stack",
                        "learning_objective": "Understand encapsulation, decapsulation, PDUs (Packet, Frame, Segment), and layer functions.",
                        "subtopics": ["Application, Presentation, Session, Transport, Network, Data Link, Physical layers", "Headers and trailers during encapsulation", "Wireshark packet capture basics"],
                        "practice_tasks": ["Map common protocols (HTTP, TCP, IP, Ethernet) to their respective OSI layers", "Capture and inspect an ICMP ping packet in Wireshark"],
                        "resources": [
                            {"title": "OSI Model Explained - Cloudflare", "url": "https://www.cloudflare.com/learning/ddos/glossary/open-systems-interconnection-model-osi/", "language": "English", "resource_type": "article", "source": "Cloudflare"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "IPv4 Addressing, Subnetting & CIDR Notation",
                        "learning_objective": "Calculate network addresses, broadcast addresses, usable host ranges, and subnet masks.",
                        "subtopics": ["Classful vs Classless Inter-Domain Routing (CIDR)", "Subnet masks (e.g. /24, /27, /30)", "Public vs Private IP ranges (RFC 1918)"],
                        "practice_tasks": ["Subnet a 192.168.1.0/24 network into 4 equal subnets", "Find network ID and broadcast ID for 10.50.12.78/22"],
                        "resources": [
                            {"title": "IPv4 Subnetting Guide", "url": "https://www.geeksforgeeks.org/ip-addressing-and-subnetting-basics/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    },
                    {
                        "day_number": 3,
                        "topic": "Transport Layer: TCP vs UDP & The 3-Way Handshake",
                        "learning_objective": "Understand connection-oriented vs connectionless communication, ports, and socket connections.",
                        "subtopics": ["SYN, SYN-ACK, ACK handshake sequence", "FIN-ACK connection termination", "UDP lightweight datagram header"],
                        "practice_tasks": ["Draw sequence diagram of TCP 3-way handshake and 4-way teardown", "Explain why video streaming and DNS use UDP"],
                        "resources": [
                            {"title": "TCP 3-Way Handshake Process", "url": "https://www.geeksforgeeks.org/tcp-3-way-handshake-process/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    },
                    {
                        "day_number": 4,
                        "topic": "TCP Reliability: Sliding Window, Flow Control & Congestion Control",
                        "learning_objective": "Analyze how TCP prevents packet loss and network congestion using dynamic windows.",
                        "subtopics": ["Sliding Window Protocol", "Flow control with Receiver Window (rwnd)", "Congestion control: Slow Start, Congestion Avoidance, Fast Retransmit"],
                        "practice_tasks": ["Trace TCP window size progression after a packet drop", "Compare Go-Back-N vs Selective Repeat ARQ"],
                        "resources": [
                            {"title": "TCP Congestion Control Algorithms", "url": "https://en.wikipedia.org/wiki/TCP_congestion_control", "language": "English", "resource_type": "doc", "source": "Wikipedia"}
                        ]
                    },
                    {
                        "day_number": 5,
                        "topic": "Network Layer: Routing Protocols (RIP, OSPF, BGP) & NAT",
                        "learning_objective": "Understand packet routing, distance-vector vs link-state algorithms, and Network Address Translation.",
                        "subtopics": ["Routing Information Protocol (RIP) vs OSPF", "Border Gateway Protocol (BGP) for autonomous systems", "Network Address Translation (NAT) & PAT (Port Address Translation)"],
                        "practice_tasks": ["Explain how a home router NAT translates private IP to public IP", "Describe Dijkstra's role in OSPF link-state routing"],
                        "resources": [
                            {"title": "Routing Protocols Explained", "url": "https://www.geeksforgeeks.org/routing-protocols-in-computer-networks/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"}
                        ]
                    }
                ]
            },
            {
                "phase_number": 2,
                "title": "Application Layer Protocols, DNS, HTTP/HTTPS & WebSockets",
                "description": "Domain Name System, HTTP/1.1 vs HTTP/2 vs HTTP/3, TLS handshake, and real-time socket connections.",
                "project_title": "Multi-Client Asynchronous Chat & HTTP Server",
                "project_description": "Build an HTTP server from raw sockets and a real-time multi-room chat server using WebSockets.",
                "days": [
                    {
                        "day_number": 6,
                        "topic": "Domain Name System (DNS) Resolution Architecture",
                        "learning_objective": "Trace recursive and iterative DNS queries from root nameservers to TLDs to authoritative nameservers.",
                        "subtopics": ["DNS record types (A, AAAA, CNAME, MX, TXT, NS)", "DNS caching at browser, OS, and ISP resolver", "DNS propagation and TTL"],
                        "practice_tasks": ["Use dig / nslookup to trace complete DNS resolution for a domain", "Configure a CNAME record pointing to a CDN"],
                        "resources": [
                            {"title": "What is DNS? - Cloudflare", "url": "https://www.cloudflare.com/learning/dns/what-is-dns/", "language": "English", "resource_type": "article", "source": "Cloudflare"}
                        ]
                    },
                    {
                        "day_number": 7,
                        "topic": "HTTP/1.1, HTTP/2 & HTTP/3 Evolution",
                        "learning_objective": "Analyze HTTP request/response headers, status codes, pipelining, multiplexing, and QUIC.",
                        "subtopics": ["HTTP methods (GET, POST, PUT, DELETE, PATCH, OPTIONS)", "HTTP/1.1 Head-of-Line blocking", "HTTP/2 binary framing and multiplexing", "HTTP/3 over UDP with QUIC"],
                        "practice_tasks": ["Inspect HTTP/2 multiplexed network streams in Chrome DevTools", "Compare HTTP headers vs payload bandwidth overhead"],
                        "resources": [
                            {"title": "An Overview of HTTP - MDN", "url": "https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview", "language": "English", "resource_type": "doc", "source": "MDN"}
                        ]
                    },
                    {
                        "day_number": 8,
                        "topic": "TLS / SSL Handshake & HTTPS Encryption",
                        "learning_objective": "Understand public key cryptography, digital certificates, Certificate Authorities, and symmetric session keys.",
                        "subtopics": ["Symmetric (AES) vs Asymmetric (RSA / ECC) encryption", "TLS 1.3 handshake sequence", "Role of Root CAs and Certificate Chains"],
                        "practice_tasks": ["Inspect SSL certificate chain of a website via browser security tab", "Generate a self-signed certificate using OpenSSL"],
                        "resources": [
                            {"title": "How Does SSL/TLS Work? - Cloudflare", "url": "https://www.cloudflare.com/learning/ssl/how-does-ssl-work/", "language": "English", "resource_type": "article", "source": "Cloudflare"}
                        ]
                    },
                    {
                        "day_number": 9,
                        "topic": "Socket Programming in Python / Java",
                        "learning_objective": "Establish low-level TCP and UDP client/server socket connections and send raw byte buffers.",
                        "subtopics": ["socket(), bind(), listen(), accept(), connect()", "Non-blocking sockets and select/poll", "Handling packet framing and delimiters"],
                        "practice_tasks": ["Write a TCP echo server handling 5 simultaneous client connections", "Build a simple UDP heartbeat sender and receiver"],
                        "resources": [
                            {"title": "Socket Programming in Python - Real Python", "url": "https://realpython.com/python-sockets/", "language": "English", "resource_type": "article", "source": "RealPython"}
                        ]
                    },
                    {
                        "day_number": 10,
                        "topic": "WebSockets & Real-Time Bidirectional Communication",
                        "learning_objective": "Understand HTTP Upgrade handshake, persistent TCP connection, and WebSocket framing.",
                        "subtopics": ["HTTP 101 Switching Protocols header", "Full-duplex frame streaming", "Comparison: Polling vs Long-Polling vs Server-Sent Events vs WebSockets"],
                        "practice_tasks": ["Build a simple real-time chat between two browser tabs using WebSockets", "Benchmark WebSocket message latency against HTTP polling"],
                        "resources": [
                            {"title": "The WebSocket API - MDN", "url": "https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API", "language": "English", "resource_type": "doc", "source": "MDN"}
                        ]
                    }
                ]
            }
        ]
    },
    "system-design-cse": {
        "id": "system-design-cse",
        "title": "System Design & Distributed Systems",
        "category": "Core CSE",
        "badge": "Advanced Core",
        "description": "Scalability principles, load balancing, caching strategies, database sharding, message queues, and microservices architecture.",
        "skills": ["System Design", "Scalability", "Microservices", "Load Balancing", "Caching", "Message Queues", "CAP Theorem"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Scalability Building Blocks: Load Balancing, Caching & Sharding",
                "description": "Horizontal scaling, reverse proxies (Nginx), CDN caching, and database partitioning.",
                "project_title": "Design a Distributed URL Shortener (TinyURL) Architecture",
                "project_description": "Architect a high-traffic URL shortener with Base62 encoding, distributed ID generation, and Redis caching.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Vertical vs Horizontal Scaling & Latency vs Throughput",
                        "learning_objective": "Understand trade-offs of scaling out, bottlenecks, SPOFs (Single Point of Failure), and SLOs.",
                        "subtopics": ["Vertical scaling limits", "Horizontal scaling stateless services", "SLA, SLO, SLI definitions"],
                        "practice_tasks": ["Calculate throughput needed for 100M daily active users", "Identify SPOFs in a basic web architecture diagram"],
                        "resources": [
                            {"title": "System Design Primer - GitHub", "url": "https://github.com/donnemartin/system-design-primer", "language": "English", "resource_type": "doc", "source": "Donne Martin"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Load Balancers (Layer 4 vs Layer 7) & Reverse Proxies",
                        "learning_objective": "Configure load balancing algorithms (Round Robin, Least Connections, IP Hash) and understand Nginx.",
                        "subtopics": ["L4 (Transport level) vs L7 (Application level) load balancing", "Health checks and failovers", "Reverse proxy caching and SSL termination"],
                        "practice_tasks": ["Write an Nginx config for round-robin balancing across 3 upstream servers", "Compare weighted round robin with least response time"],
                        "resources": [
                            {"title": "What is Load Balancing? - NGINX", "url": "https://www.nginx.com/resources/glossary/load-balancing/", "language": "English", "resource_type": "article", "source": "NGINX"}
                        ]
                    },
                    {
                        "day_number": 3,
                        "topic": "Caching Strategies: Cache-Aside, Write-Through & Eviction",
                        "learning_objective": "Accelerate read-heavy architectures with Redis, memcached, and CDN edge networks.",
                        "subtopics": ["Cache-Aside (Lazy loading) pattern", "Write-Through vs Write-Behind caching", "Cache Stampede, Cache Penetration, and Cache Avalanche"],
                        "practice_tasks": ["Implement Cache-Aside pattern in Python with a fallback DB query", "Design solution for cache stampede using distributed locks"],
                        "resources": [
                            {"title": "Caching Architecture and Patterns", "url": "https://aws.amazon.com/caching/best-practices/", "language": "English", "resource_type": "article", "source": "AWS"}
                        ]
                    },
                    {
                        "day_number": 4,
                        "topic": "Database Sharding & Consistent Hashing",
                        "learning_objective": "Partition databases across multiple nodes and route requests using consistent hashing.",
                        "subtopics": ["Vertical vs Horizontal database partitioning", "Consistent hashing ring and virtual nodes", "Handling re-sharding and data migration"],
                        "practice_tasks": ["Implement a consistent hashing ring in code with 3 server nodes", "Explain why modulus hashing fails when adding new server nodes"],
                        "resources": [
                            {"title": "Consistent Hashing Guide - System Design Primer", "url": "https://github.com/donnemartin/system-design-primer#consistent-hashing", "language": "English", "resource_type": "doc", "source": "System Design Primer"}
                        ]
                    },
                    {
                        "day_number": 5,
                        "topic": "Message Queues & Asynchronous Processing (Kafka, RabbitMQ)",
                        "learning_objective": "Decouple microservices using pub/sub queues, event streaming, and dead letter queues.",
                        "subtopics": ["Point-to-point queues vs Publish/Subscribe", "Kafka log-based architecture & partitions", "Handling idempotent message processing"],
                        "practice_tasks": ["Design an order processing workflow using a background message queue", "Write consumer logic ensuring duplicate messages don't double-charge customers"],
                        "resources": [
                            {"title": "Apache Kafka Architecture Overview", "url": "https://kafka.apache.org/intro", "language": "English", "resource_type": "doc", "source": "Apache Kafka"}
                        ]
                    }
                ]
            },
            {
                "phase_number": 2,
                "title": "Real-World System Architectures (Chat, Social Feed, Video Streaming)",
                "description": "Design WhatsApp, Twitter/X Newsfeed, and YouTube streaming services with high availability.",
                "project_title": "Design a Distributed Real-Time Chat & Notification Engine",
                "project_description": "Full end-to-end design document covering API endpoints, DB schema, WebSocket connections, and message fan-out.",
                "days": [
                    {
                        "day_number": 6,
                        "topic": "Designing a Distributed Unique ID Generator (Snowflake)",
                        "learning_objective": "Generate 64-bit unique, roughly sortable IDs across thousands of distributed machines.",
                        "subtopics": ["Why UUIDv4 is suboptimal for clustered database indexing", "Twitter Snowflake bit allocation: Timestamp, Worker ID, Sequence", "Clock skew mitigation"],
                        "practice_tasks": ["Implement a Snowflake ID generator in code", "Calculate how many unique IDs can be generated per millisecond"],
                        "resources": [
                            {"title": "Twitter Snowflake Announcement", "url": "https://blog.twitter.com/engineering/en_us/a/2010/announcing-snowflake", "language": "English", "resource_type": "article", "source": "Twitter Eng"}
                        ]
                    },
                    {
                        "day_number": 7,
                        "topic": "Designing a Real-Time Messenger (WhatsApp / Telegram)",
                        "learning_objective": "Architect 1-on-1 and group messaging, online presence, and message delivery receipts.",
                        "subtopics": ["WebSocket gateway connection management", "Message storage in wide-column store (Cassandra)", "Delivered and Read receipt state machines"],
                        "practice_tasks": ["Draw architecture diagram for WhatsApp showing message routing across 2 clients", "Calculate storage requirements for 10 billion messages/day"],
                        "resources": [
                            {"title": "Design a Chat System - ByteByteGo", "url": "https://bytebytego.com/courses/system-design-interview/design-a-chat-system", "language": "English", "resource_type": "doc", "source": "ByteByteGo"}
                        ]
                    },
                    {
                        "day_number": 8,
                        "topic": "Designing a Social Media Newsfeed (Twitter / Instagram)",
                        "learning_objective": "Compare Fanout-on-Write (Push) vs Fanout-on-Read (Pull) for celebrity accounts.",
                        "subtopics": ["Timeline generation and caching in Redis", "Fanout-on-Write for normal users", "Hybrid fanout model for high-follower accounts"],
                        "practice_tasks": ["Analyze push vs pull models for a user with 50M followers", "Design Redis sorted sets data structure for user timelines"],
                        "resources": [
                            {"title": "Designing a News Feed System - ByteByteGo", "url": "https://bytebytego.com/courses/system-design-interview/design-a-news-feed-system", "language": "English", "resource_type": "doc", "source": "ByteByteGo"}
                        ]
                    },
                    {
                        "day_number": 9,
                        "topic": "Designing a Video Streaming Platform (YouTube / Netflix)",
                        "learning_objective": "Handle video chunking, transcoding pipelines, adaptive bitrate streaming, and CDN edge delivery.",
                        "subtopics": ["HLS (HTTP Live Streaming) and MPEG-DASH", "Asynchronous video transcoding queue", "Geo-distributed CDN video delivery"],
                        "practice_tasks": ["Draw the video upload and processing ingestion pipeline", "Explain how adaptive bitrate switches from 1080p to 480p automatically"],
                        "resources": [
                            {"title": "Design YouTube - System Design Interview", "url": "https://github.com/donnemartin/system-design-primer/blob/master/solutions/system_design/scaling_aws/README.md", "language": "English", "resource_type": "doc", "source": "System Design Primer"}
                        ]
                    },
                    {
                        "day_number": 10,
                        "topic": "Designing a Distributed Rate Limiter & API Gateway",
                        "learning_objective": "Implement Token Bucket and Leaky Bucket rate limiting to guard public APIs.",
                        "subtopics": ["Token Bucket vs Leaky Bucket vs Sliding Window Log", "Centralized rate limiting with Redis Lua scripts", "API Gateway duties (Auth, Rate Limiting, SSL, Routing)"],
                        "practice_tasks": ["Write a Token Bucket algorithm in code", "Write Redis Lua script to atomically increment request count with expiry"],
                        "resources": [
                            {"title": "Rate Limiting Fundamentals - Cloudflare", "url": "https://www.cloudflare.com/learning/bots/what-is-rate-limiting/", "language": "English", "resource_type": "article", "source": "Cloudflare"}
                        ]
                    }
                ]
            }
        ]
    },

    # =========================================================================
    # SEPARATE PROGRAMMING LANGUAGES & ADDITIONAL TRACKS
    # =========================================================================
    "python-lang": {
        "id": "python-lang",
        "title": "Python Programming (Beginner to Advanced)",
        "category": "Languages",
        "badge": "Beginner Friendly",
        "description": "Master Python syntax, data types, OOP, generators, decorators, file I/O, error handling, and standard library modules.",
        "skills": ["Python 3", "Object-Oriented Programming", "Functional Programming", "Decorators & Generators", "File I/O & JSON", "Unit Testing"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Python Syntax, Data Structures & Functions",
                "description": "Core primitives, lists, dictionaries, tuples, sets, functions, and recursion.",
                "project_title": "CLI Student Gradebook & Statistical Analyzer",
                "project_description": "Build an interactive CLI tool that calculates CGPA, median marks, and exports student report cards in JSON.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Python Fundamentals, Types & Control Flow",
                        "learning_objective": "Understand dynamic typing, operators, truthiness, conditional branching, and loops in Python.",
                        "subtopics": ["Primitive types & type casting", "if/elif/else conditions", "for and while loops with range/enumerate", "f-strings formatting"],
                        "practice_tasks": ["Write a prime number generator up to N", "Implement a command-line rock-paper-scissors game with score tracking"],
                        "resources": [
                            {"title": "Python for Beginners — Full Course", "url": "https://www.youtube.com/watch?v=kqtD5dpn9C8", "language": "English", "resource_type": "video", "source": "FreeCodeCamp"},
                            {"title": "Python Tutorial in Telugu (Complete Course)", "url": "https://www.youtube.com/results?search_query=python+tutorial+in+telugu+vamsi+bhavani", "language": "Telugu", "resource_type": "video", "source": "Vamsi Bhavani"},
                            {"title": "Python Full Course in Hindi", "url": "https://www.youtube.com/results?search_query=python+full+course+in+hindi+codewithharry", "language": "Hindi", "resource_type": "video", "source": "CodeWithHarry"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Python Built-in Data Structures",
                        "learning_objective": "Master Lists, Tuples, Dictionaries, and Sets with list comprehensions and dict manipulations.",
                        "subtopics": ["List slicing & in-place methods", "Dictionary operations & dict comprehensions", "Set operations (union, intersection, difference)", "Tuple unpacking"],
                        "practice_tasks": ["Implement word frequency counter on a multiline text", "Flatten a nested list using list comprehension"],
                        "resources": [
                            {"title": "Python Data Structures Mastery", "url": "https://docs.python.org/3/tutorial/datastructures.html", "language": "English", "resource_type": "doc", "source": "Python Docs"},
                            {"title": "Python Lists & Dictionaries in Telugu", "url": "https://www.youtube.com/results?search_query=python+lists+dictionaries+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Python Collections & Loops in Hindi", "url": "https://www.youtube.com/results?search_query=python+lists+and+dictionary+in+hindi", "language": "Hindi", "resource_type": "video", "source": "YouTube Hindi"}
                        ]
                    },
                    {
                        "day_number": 3,
                        "topic": "Functions, Scope, *args & **kwargs",
                        "learning_objective": "Master function definition, default arguments, variadic parameters, keyword arguments, and closures.",
                        "subtopics": ["Positional vs Keyword arguments", "Unpacking with * and **", "LEGB Scope rule", "Lambda functions & map/filter"],
                        "practice_tasks": ["Build a generic arithmetic pipeline taking arbitrary functions", "Write a memoization function without using functools"],
                        "resources": [
                            {"title": "Python Functions Deep Dive", "url": "https://realpython.com/defining-your-own-python-function/", "language": "English", "resource_type": "article", "source": "RealPython"},
                            {"title": "Python Functions in Telugu", "url": "https://www.youtube.com/results?search_query=python+functions+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Python Functions in Hindi", "url": "https://www.youtube.com/results?search_query=python+functions+in+hindi", "language": "Hindi", "resource_type": "video", "source": "CodeWithHarry"}
                        ]
                    }
                ]
            },
            {
                "phase_number": 2,
                "title": "Object-Oriented Python, Generators & Decorators",
                "description": "Classes, magic methods, inheritance, polymorphism, decorators, generators, and context managers.",
                "project_title": "Custom ORM & Mini Query Builder",
                "project_description": "Build an in-memory database simulation using Python classes, decorators for validation, and generator pipelines.",
                "days": [
                    {
                        "day_number": 4,
                        "topic": "Object-Oriented Programming in Python",
                        "learning_objective": "Design clean classes using __init__, encapsulation, inheritance, method overriding, and classmethods.",
                        "subtopics": ["Classes vs Instances", "Magic methods (__repr__, __str__, __len__, __eq__)", "Classmethods and staticmethods", "@property decorator"],
                        "practice_tasks": ["Build a Bank Account hierarchy with Savings and Current accounts", "Implement a Vector2D class supporting +, -, and * operators via magic methods"],
                        "resources": [
                            {"title": "OOP in Python Tutorial", "url": "https://realpython.com/python3-object-oriented-programming/", "language": "English", "resource_type": "article", "source": "RealPython"},
                            {"title": "Python OOP in Telugu", "url": "https://www.youtube.com/results?search_query=python+oop+in+telugu", "language": "Telugu", "resource_type": "video", "source": "Vamsi Bhavani"},
                            {"title": "Python OOPS in Hindi", "url": "https://www.youtube.com/results?search_query=python+oops+in+hindi+codewithharry", "language": "Hindi", "resource_type": "video", "source": "CodeWithHarry"}
                        ]
                    },
                    {
                        "day_number": 5,
                        "topic": "Decorators, Generators & Context Managers",
                        "learning_objective": "Master metaprogramming with decorators, lazy evaluation with yield, and resource management with context managers.",
                        "subtopics": ["Function wrapping with @wraps", "Decorators with arguments", "Generator functions and generator expressions", "__enter__ and __exit__ protocol"],
                        "practice_tasks": ["Write an @execution_timer decorator measuring function runtime", "Build an infinite Fibonacci generator with lazy evaluation"],
                        "resources": [
                            {"title": "Python Decorators & Generators", "url": "https://www.youtube.com/watch?v=FsAPt_9Bf3U", "language": "English", "resource_type": "video", "source": "Corey Schafer"},
                            {"title": "Decorators & Generators in Telugu", "url": "https://www.youtube.com/results?search_query=python+decorators+generators+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Python Decorators in Hindi", "url": "https://www.youtube.com/results?search_query=python+decorators+in+hindi", "language": "Hindi", "resource_type": "video", "source": "CodeWithHarry"}
                        ]
                    }
                ]
            }
        ]
    },

    "java-lang": {
        "id": "java-lang",
        "title": "Java Core & OOP Mastery",
        "category": "Languages",
        "badge": "Enterprise Standard",
        "description": "Comprehensive Java curriculum: JVM memory architecture, OOP principles, Collections Framework, Multithreading, and Streams API.",
        "skills": ["Java 17/21", "JVM Architecture", "Collections Framework", "Concurrency & Threads", "Java Streams & Lambdas", "Generics"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Core Syntax, Memory Model & Object-Oriented Java",
                "description": "Variables, JVM stack vs heap, class design, interfaces, abstract classes, and exception handling.",
                "project_title": "Enterprise Inventory Management Engine",
                "project_description": "Implement a type-safe inventory management system utilizing interfaces, custom exceptions, and polymorphic billing.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Java Environment, JVM Stack vs Heap & Primitives",
                        "learning_objective": "Understand the bytecode lifecycle, classloaders, stack vs heap allocation, and garbage collection basics.",
                        "subtopics": ["JDK vs JRE vs JVM", "Primitive vs Reference types", "Stack memory vs Heap memory", "String constant pool & Immutability"],
                        "practice_tasks": ["Write a program illustrating pass-by-value in Java with object references", "Demonstrate String vs StringBuilder memory efficiency in a 100k iteration benchmark"],
                        "resources": [
                            {"title": "Java Full Course — FreeCodeCamp", "url": "https://www.youtube.com/watch?v=A74TOX803D0", "language": "English", "resource_type": "video", "source": "FreeCodeCamp"},
                            {"title": "Java Programming in Telugu Complete", "url": "https://www.youtube.com/results?search_query=java+programming+in+telugu+vamsi+bhavani", "language": "Telugu", "resource_type": "video", "source": "Vamsi Bhavani"},
                            {"title": "Java Full Course in Hindi", "url": "https://www.youtube.com/results?search_query=java+full+course+in+hindi+apna+college", "language": "Hindi", "resource_type": "video", "source": "Apna College"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "OOP in Java: Inheritance, Polymorphism & Interfaces",
                        "learning_objective": "Design modular enterprise software using interfaces, abstract classes, and polymorphism.",
                        "subtopics": ["Method overloading vs overriding", "abstract classes vs interface default methods", "Access modifiers & encapsulation", "Diamond problem resolution"],
                        "practice_tasks": ["Design a Payment Gateway with CreditCard, UPI, and NetBanking implementations", "Implement an immutable Employee class with deep copy"],
                        "resources": [
                            {"title": "Java OOP Concepts Deep Dive", "url": "https://www.geeksforgeeks.org/object-oriented-programming-oops-concept-in-java/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"},
                            {"title": "Java OOPS in Telugu", "url": "https://www.youtube.com/results?search_query=java+oops+concepts+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Java OOPs in Hindi", "url": "https://www.youtube.com/results?search_query=java+oops+in+hindi+codehelp", "language": "Hindi", "resource_type": "video", "source": "CodeHelp"}
                        ]
                    }
                ]
            },
            {
                "phase_number": 2,
                "title": "Collections, Streams API & Multithreading",
                "description": "Lists, Maps, Sets, Comparator/Comparable, Lambdas, Functional Interfaces, and Concurrency primitives.",
                "project_title": "Concurrent Thread-Safe Cache & Analytics Engine",
                "project_description": "Build an in-memory concurrent key-value cache using ConcurrentHashMap, ReentrantLocks, and Java Streams.",
                "days": [
                    {
                        "day_number": 3,
                        "topic": "Java Collections Framework & Generics",
                        "learning_objective": "Master ArrayList, LinkedList, HashMap internal hashing, TreeSet, and generic type bounds.",
                        "subtopics": ["HashMap internal buckets & collision resolution", "Comparable vs Comparator", "Generic classes & wildcards", "Fail-fast vs Fail-safe iterators"],
                        "practice_tasks": ["Implement an LRU Cache using LinkedHashMap", "Sort custom objects by multiple criteria using Comparator chaining"],
                        "resources": [
                            {"title": "Java Collections Framework Explained", "url": "https://www.youtube.com/watch?v=rzA7UJ-hQn4", "language": "English", "resource_type": "video", "source": "Telusko"},
                            {"title": "Java Collections in Telugu", "url": "https://www.youtube.com/results?search_query=java+collections+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Java Collections Framework in Hindi", "url": "https://www.youtube.com/results?search_query=java+collections+in+hindi", "language": "Hindi", "resource_type": "video", "source": "Durga Software / Hindi"}
                        ]
                    },
                    {
                        "day_number": 4,
                        "topic": "Java Streams API, Lambdas & Concurrency",
                        "learning_objective": "Leverage functional programming with map/filter/reduce and build multithreaded applications.",
                        "subtopics": ["Stream pipeline (intermediate vs terminal)", "Collectors (groupingBy, partitioningBy)", "Thread lifecycle & ExecutorService", "Synchronized blocks & ReentrantLock"],
                        "practice_tasks": ["Group a list of transactions by city and calculate total revenue using Streams", "Implement a Producer-Consumer pattern using BlockingQueue and thread pools"],
                        "resources": [
                            {"title": "Java 8 Streams and Lambdas in Depth", "url": "https://www.youtube.com/watch?v=t1-YZ6bF-g0", "language": "English", "resource_type": "video", "source": "Java Brains"},
                            {"title": "Java Streams and Multithreading in Telugu", "url": "https://www.youtube.com/results?search_query=java+threads+streams+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Java Multithreading in Hindi", "url": "https://www.youtube.com/results?search_query=java+multithreading+in+hindi", "language": "Hindi", "resource_type": "video", "source": "CodeWithHarry"}
                        ]
                    }
                ]
            }
        ]
    },

    "cpp-lang": {
        "id": "cpp-lang",
        "title": "C++ Programming & STL Mastery",
        "category": "Languages",
        "badge": "High Performance",
        "description": "Modern C++ (C++17/20), pointer mechanics, references, manual memory allocation, RAII, templates, and STL algorithms.",
        "skills": ["Modern C++", "Pointers & References", "Manual Memory Management", "STL Algorithms", "Object-Oriented C++", "Templates"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Pointers, References & Object-Oriented C++",
                "description": "Memory addresses, pointer arithmetic, dynamic memory allocation with new/delete, RAII, and class design.",
                "project_title": "Custom Smart Pointer Library & String Class",
                "project_description": "Implement custom unique_ptr and shared_ptr smart pointer classes with reference counting and automatic destruction.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "C++ Syntax, Pointers & Memory Addresses",
                        "learning_objective": "Understand stack vs heap in C++, dereferencing pointers, and pointer arithmetic.",
                        "subtopics": ["Address-of (&) and dereference (*) operators", "Pointers to pointers", "Pointer arithmetic and array decay", "const pointers vs pointer to const"],
                        "practice_tasks": ["Write an in-place string reverse using raw pointers", "Implement dynamic 2D array allocation using double pointers and free it cleanly"],
                        "resources": [
                            {"title": "C++ Tutorial for Beginners — Full Course", "url": "https://www.youtube.com/watch?v=vLnPwxZdW4Y", "language": "English", "resource_type": "video", "source": "FreeCodeCamp"},
                            {"title": "C++ in Telugu Complete Playlist", "url": "https://www.youtube.com/results?search_query=c%2B%2B+programming+in+telugu+vamsi+bhavani", "language": "Telugu", "resource_type": "video", "source": "Vamsi Bhavani"},
                            {"title": "C++ Full Course in Hindi", "url": "https://www.youtube.com/results?search_query=c%2B%2B+full+course+in+hindi+codewithharry", "language": "Hindi", "resource_type": "video", "source": "CodeWithHarry"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "STL Containers & Algorithms",
                        "learning_objective": "Master vector, map, set, priority_queue, sort, lower_bound, and lambda predicates.",
                        "subtopics": ["Vector dynamic capacity & iterator invalidation", "std::map (Red-Black tree) vs std::unordered_map", "std::priority_queue heap operations", "std::sort and custom comparator functions"],
                        "practice_tasks": ["Implement Top-K Frequent Elements using priority_queue", "Solve binary search range queries using lower_bound and upper_bound"],
                        "resources": [
                            {"title": "C++ STL Masterclass (Containers & Algorithms)", "url": "https://www.youtube.com/watch?v=zBhVZzi5RdU", "language": "English", "resource_type": "video", "source": "Striver (take U forward)"},
                            {"title": "C++ STL in Telugu", "url": "https://www.youtube.com/results?search_query=c%2B%2B+stl+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "C++ STL in Hindi — Love Babbar", "url": "https://www.youtube.com/results?search_query=c%2B%2B+stl+in+hindi+love+babbar", "language": "Hindi", "resource_type": "video", "source": "Love Babbar"}
                        ]
                    }
                ]
            }
        ]
    },

    "c-lang": {
        "id": "c-lang",
        "title": "C Programming & Low-Level Foundations",
        "category": "Languages",
        "badge": "Core Systems",
        "description": "Fundamental programming in C: memory layout, pointers, structures, bitwise manipulation, dynamic memory allocation, and compilation pipeline.",
        "skills": ["C Language", "Memory Addresses", "malloc/free", "Structures & Unions", "Bit Manipulation", "File I/O"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Syntax, Pointers & Dynamic Memory Allocation",
                "description": "Pointers, arrays, string manipulation, malloc/calloc/realloc/free, and memory leaks.",
                "project_title": "Custom Memory Allocator Simulation",
                "project_description": "Implement custom malloc() and free() functions using a linked list of free memory blocks.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "C Fundamentals, Memory Layout & Pointers",
                        "learning_objective": "Understand the four memory segments: Stack, Heap, Data (BSS), and Text, and raw pointer access.",
                        "subtopics": ["Variable declarations and storage classes", "Pointers and memory addresses", "Pass-by-reference using pointers", "Preprocessors and header files"],
                        "practice_tasks": ["Swap two numbers using pointer references without helper variable", "Write custom strlen() and strcpy() using pointer traversal"],
                        "resources": [
                            {"title": "C Programming Tutorial for Beginners", "url": "https://www.youtube.com/watch?v=KJgsSFOSQv0", "language": "English", "resource_type": "video", "source": "FreeCodeCamp"},
                            {"title": "C Language in Telugu Complete", "url": "https://www.youtube.com/results?search_query=c+programming+in+telugu+vamsi+bhavani", "language": "Telugu", "resource_type": "video", "source": "Vamsi Bhavani"},
                            {"title": "C Language Full Course in Hindi", "url": "https://www.youtube.com/results?search_query=c+programming+full+course+in+hindi+codewithharry", "language": "Hindi", "resource_type": "video", "source": "CodeWithHarry"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Dynamic Memory Allocation & Structures",
                        "learning_objective": "Master malloc, calloc, realloc, free, structs, typedef, and struct pointers.",
                        "subtopics": ["malloc() vs calloc()", "realloc() and memory fragmentation", "struct memory alignment & padding", "Dangling pointers and memory leaks (valgrind)"],
                        "practice_tasks": ["Build a dynamic array of student structs that doubles in size when full", "Write a linked list in C from scratch with insert and delete operations"],
                        "resources": [
                            {"title": "Dynamic Memory Allocation in C", "url": "https://www.geeksforgeeks.org/dynamic-memory-allocation-in-c-using-malloc-calloc-free-realloc/", "language": "English", "resource_type": "article", "source": "GeeksforGeeks"},
                            {"title": "Pointers and Dynamic Memory in Telugu", "url": "https://www.youtube.com/results?search_query=c+pointers+dynamic+memory+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Structures and Pointers in C in Hindi", "url": "https://www.youtube.com/results?search_query=c+structures+and+pointers+in+hindi", "language": "Hindi", "resource_type": "video", "source": "CodeWithHarry"}
                        ]
                    }
                ]
            }
        ]
    },

    "js-lang": {
        "id": "js-lang",
        "title": "Modern JavaScript (ES6+ & Async)",
        "category": "Languages",
        "badge": "Web Foundation",
        "description": "Master Modern JavaScript: Event loop, asynchronous execution, closures, prototypes, Promises, async/await, and ES6+ features.",
        "skills": ["JavaScript ES6+", "Event Loop", "Promises & Async/Await", "Closures & Scope", "DOM Manipulation", "Fetch API"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Core JS Mechanics, Closures & Asynchronous Flow",
                "description": "Call stack, event loop, hoisting, closures, Promises, async/await, and modern ES6 syntax.",
                "project_title": "Real-Time Weather & Currency Conversion Dashboard",
                "project_description": "Build an interactive web dashboard utilizing async/await, fetch API, local storage, and debounced search.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "JavaScript Engine, Execution Context & Event Loop",
                        "learning_objective": "Understand the call stack, web APIs, callback queue, microtask queue, and how JS runs single-threaded.",
                        "subtopics": ["Execution context and variable environment", "Hoisting behavior of var vs let/const", "Call stack vs Microtask vs Macrotask queue", "Temporal Dead Zone (TDZ)"],
                        "practice_tasks": ["Predict execution order of synchronous, setTimeout, and Promise code snippets", "Write a custom debounce function using closures and setTimeout"],
                        "resources": [
                            {"title": "What the heck is the event loop anyway? — Philip Roberts", "url": "https://www.youtube.com/watch?v=8aGhZQkoFbQ", "language": "English", "resource_type": "video", "source": "JSConf"},
                            {"title": "JavaScript Tutorial in Telugu Complete", "url": "https://www.youtube.com/results?search_query=javascript+tutorial+in+telugu+vamsi+bhavani", "language": "Telugu", "resource_type": "video", "source": "Vamsi Bhavani"},
                            {"title": "Namaste JavaScript by Akshay Saini", "url": "https://www.youtube.com/playlist?list=PLlasXeu85E9cQ32gLCvAvr9vNaUccPVNP", "language": "Hindi", "resource_type": "video", "source": "Akshay Saini"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Promises, Async/Await & Fetch API",
                        "learning_objective": "Handle complex asynchronous workflows cleanly using Promise.all, Promise.allSettled, and async/await.",
                        "subtopics": ["Promise lifecycle states (pending, fulfilled, rejected)", "Chaining .then() and .catch()", "async/await error handling with try/catch", "Fetch API with headers, POST requests, and JSON parsing"],
                        "practice_tasks": ["Build an API client that retries a failed request up to 3 times with exponential backoff", "Fetch data from multiple endpoints concurrently using Promise.all and combine results"],
                        "resources": [
                            {"title": "JavaScript Promises in 10 Minutes", "url": "https://www.youtube.com/watch?v=DHvZLI7DbU0", "language": "English", "resource_type": "video", "source": "Web Dev Simplified"},
                            {"title": "JavaScript Async Await in Telugu", "url": "https://www.youtube.com/results?search_query=javascript+async+await+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Async Await & Promises in Hindi", "url": "https://www.youtube.com/results?search_query=javascript+promises+async+await+in+hindi+chai+aur+code", "language": "Hindi", "resource_type": "video", "source": "Chai aur Code"}
                        ]
                    }
                ]
            }
        ]
    },

    "sql-lang": {
        "id": "sql-lang",
        "title": "SQL & Relational Database Querying",
        "category": "Languages",
        "badge": "High Demand",
        "description": "Comprehensive SQL mastery: complex joins, subqueries, aggregations, window functions, indexing, transactions, and performance tuning.",
        "skills": ["SQL", "PostgreSQL", "Window Functions", "Complex Joins", "Indexes & Query Plans", "Database Transactions"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Data Querying, Joins & Window Functions",
                "description": "SELECT queries, WHERE filtering, INNER/LEFT/RIGHT/FULL joins, GROUP BY, and window functions.",
                "project_title": "E-Commerce Revenue & Customer Cohort Analytics",
                "project_description": "Write enterprise SQL queries analyzing user retention, monthly recurring revenue, and churn cohorts.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "SQL Data Manipulation & Multi-Table Joins",
                        "learning_objective": "Master table querying, filtering conditions, and joining multiple relational entities.",
                        "subtopics": ["INNER JOIN vs LEFT/RIGHT JOIN vs FULL OUTER JOIN", "GROUP BY and HAVING vs WHERE", "Aggregate functions (SUM, AVG, COUNT, MIN, MAX)", "Handling NULL values with COALESCE"],
                        "practice_tasks": ["Find top 5 customers with highest total spending across multiple orders", "Query products that have never been purchased using LEFT JOIN with IS NULL"],
                        "resources": [
                            {"title": "SQL Tutorial - Full Database Course for Beginners", "url": "https://www.youtube.com/watch?v=HXV3zeQKqGY", "language": "English", "resource_type": "video", "source": "FreeCodeCamp"},
                            {"title": "SQL in Telugu Complete Course", "url": "https://www.youtube.com/results?search_query=sql+full+course+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "SQL Full Course in Hindi", "url": "https://www.youtube.com/results?search_query=sql+full+course+in+hindi+apna+college", "language": "Hindi", "resource_type": "video", "source": "Apna College"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Advanced SQL: Window Functions & Common Table Expressions (CTEs)",
                        "learning_objective": "Perform advanced analytics using ROW_NUMBER, RANK, DENSE_RANK, NTILE, LAG, LEAD, and recursive CTEs.",
                        "subtopics": ["OVER (PARTITION BY ... ORDER BY ...)", "ROW_NUMBER() vs RANK() vs DENSE_RANK()", "Running totals and moving averages", "Common Table Expressions (WITH clause)"],
                        "practice_tasks": ["Calculate running total of sales day-by-day for each store", "Identify the second highest salary in each department without using subqueries in WHERE"],
                        "resources": [
                            {"title": "SQL Window Functions Explained", "url": "https://mode.com/sql-tutorial/sql-window-functions/", "language": "English", "resource_type": "article", "source": "Mode Analytics"},
                            {"title": "SQL Window Functions in Telugu", "url": "https://www.youtube.com/results?search_query=sql+window+functions+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "SQL Window Functions in Hindi", "url": "https://www.youtube.com/results?search_query=sql+window+functions+in+hindi", "language": "Hindi", "resource_type": "video", "source": "Gate Smashers"}
                        ]
                    }
                ]
            }
        ]
    },

    "golang-lang": {
        "id": "golang-lang",
        "title": "Go (Golang) Systems & Microservices",
        "category": "Languages",
        "badge": "High Concurrency",
        "description": "Build high-throughput backends with Go: Goroutines, channels, pointers, structs, interfaces, context package, and HTTP standard library.",
        "skills": ["Golang", "Goroutines & Channels", "Concurrency Patterns", "Go REST APIs", "Pointers & Structs", "Unit Testing"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Go Syntax, Structs, Pointers & Concurrency",
                "description": "Go primitives, slices, maps, structs, methods, goroutines, and channels.",
                "project_title": "Concurrent URL Health Checker & Scraper",
                "project_description": "Build a CLI tool that polls 100 website URLs concurrently using Goroutines, worker pools, and sync.WaitGroup.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Go Fundamentals, Slices, Structs & Interfaces",
                        "learning_objective": "Understand Go static typing, value vs pointer receivers, and implicit interface satisfaction.",
                        "subtopics": ["Slices vs Arrays memory layout", "structs and embedded structs (composition)", "Interfaces in Go", "Error handling idiom (val, err := ...)"],
                        "practice_tasks": ["Write an in-memory inventory store using structs and custom interfaces", "Implement a custom error type with timestamp and HTTP status code"],
                        "resources": [
                            {"title": "Go Programming by Example", "url": "https://gobyexample.com/", "language": "English", "resource_type": "doc", "source": "GoByExample"},
                            {"title": "Golang in Telugu Tutorial", "url": "https://www.youtube.com/results?search_query=golang+tutorial+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Golang Full Course in Hindi", "url": "https://www.youtube.com/results?search_query=golang+full+course+in+hindi+chai+aur+code", "language": "Hindi", "resource_type": "video", "source": "Chai aur Code"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Goroutines, Channels & Worker Pools",
                        "learning_objective": "Master lightweight green threads (Goroutines), buffered/unbuffered channels, select statements, and sync primitives.",
                        "subtopics": ["Go runtime scheduler (M:N threading)", "Unbuffered vs Buffered channels", "sync.WaitGroup and sync.Mutex", "select with timeout and default case"],
                        "practice_tasks": ["Implement a Worker Pool pattern processing jobs from a channel", "Build a rate-limited channel pipeline in Go"],
                        "resources": [
                            {"title": "Go Concurrency Patterns", "url": "https://www.youtube.com/watch?v=f6kdp27TYZs", "language": "English", "resource_type": "video", "source": "Google Tech Talks"},
                            {"title": "Goroutines and Channels in Telugu", "url": "https://www.youtube.com/results?search_query=golang+goroutines+channels+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Golang Goroutines in Hindi", "url": "https://www.youtube.com/results?search_query=golang+goroutines+in+hindi", "language": "Hindi", "resource_type": "video", "source": "YouTube Hindi"}
                        ]
                    }
                ]
            }
        ]
    },
    "android-kotlin": {
        "id": "android-kotlin",
        "title": "Android App Development (Kotlin & Jetpack Compose)",
        "category": "Mobile Development",
        "badge": "Native Android",
        "description": "Build modern, reactive Android apps with Kotlin, Jetpack Compose, Coroutines/Flow, Clean Architecture, and Retrofit.",
        "skills": ["Android", "Kotlin", "Jetpack Compose", "Coroutines", "Flow", "StateFlow", "Retrofit", "Room DB"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Kotlin Foundations & Declarative Jetpack Compose UI",
                "description": "Master Kotlin language idiomatic syntax and build declarative UIs with Jetpack Compose.",
                "project_title": "Modern Recipe & Nutrition Explorer App",
                "project_description": "Build a responsive multi-screen Compose app with dynamic lists, search filter, and custom theming.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Kotlin Idioms, Null Safety & Data Classes",
                        "learning_objective": "Master Kotlin syntax essentials: val/var, nullability operators (?, ?:), data classes, lambdas, and extension functions.",
                        "subtopics": ["Null safety and smart casting", "Data classes and sealed interfaces", "Lambdas and higher-order functions", "Kotlin extension functions"],
                        "practice_tasks": ["Create a model hierarchy using sealed interfaces and data classes", "Write extension functions for Date and String formatting"],
                        "resources": [
                            {"title": "Kotlin Official Documentation", "url": "https://kotlinlang.org/docs/home.html", "language": "English", "resource_type": "doc", "source": "KotlinLang"},
                            {"title": "Kotlin Full Course in Telugu", "url": "https://www.youtube.com/results?search_query=kotlin+full+course+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Kotlin for Android in Hindi", "url": "https://www.youtube.com/results?search_query=kotlin+android+course+in+hindi", "language": "Hindi", "resource_type": "video", "source": "YouTube Hindi"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Jetpack Compose: Layouts, Modifiers & State",
                        "learning_objective": "Build reactive UI with remember, mutableStateOf, Column/Row/Box, and LazyColumn for smooth list rendering.",
                        "subtopics": ["Composable lifecycle and recomposition", "remember and rememberSaveable", "LazyColumn vs LazyRow performance", "Modifiers chaining rules"],
                        "practice_tasks": ["Build an interactive todo item card with animated swipe dismissal", "Render an infinite scrolling feed with LazyColumn"],
                        "resources": [
                            {"title": "Jetpack Compose Pathway", "url": "https://developer.android.com/courses/pathways/compose", "language": "English", "resource_type": "doc", "source": "Android Developers"},
                            {"title": "Jetpack Compose in Telugu", "url": "https://www.youtube.com/results?search_query=jetpack+compose+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Jetpack Compose Tutorial in Hindi", "url": "https://www.youtube.com/results?search_query=jetpack+compose+in+hindi", "language": "Hindi", "resource_type": "video", "source": "YouTube Hindi"}
                        ]
                    }
                ]
            },
            {
                "phase_number": 2,
                "title": "MVVM, Coroutines, Retrofit & Room SQLite",
                "description": "Architect robust apps with StateFlow, Coroutine Dispatchers, Retrofit REST client, and Room local persistence.",
                "project_title": "Offline-First GitHub Repository Tracker",
                "project_description": "Author a complete production app that caches GitHub repos locally in Room and syncs via Retrofit.",
                "days": [
                    {
                        "day_number": 3,
                        "topic": "Kotlin Coroutines, Dispatchers & Flow / StateFlow",
                        "learning_objective": "Handle asynchronous background operations without blocking the Android main thread using suspend functions and StateFlow.",
                        "subtopics": ["Dispatchers.IO vs Dispatchers.Main", "viewModelScope and lifecycle-aware collection", "Flow transformations: map, filter, combine", "StateFlow vs SharedFlow"],
                        "practice_tasks": ["Refactor a blocking network call to async coroutine with viewModelScope", "Expose UI state as a single immutable StateFlow"],
                        "resources": [
                            {"title": "Kotlin Coroutines on Android", "url": "https://developer.android.com/kotlin/coroutines", "language": "English", "resource_type": "doc", "source": "Android Developers"}
                        ]
                    },
                    {
                        "day_number": 4,
                        "topic": "Retrofit 2 REST Integration & Room Persistence",
                        "learning_objective": "Consume REST APIs with Retrofit and Moshi/Gson, and persist structured data in Room SQLite.",
                        "subtopics": ["Retrofit OkHttpClient logging interceptors", "Room Entity, DAO and Database classes", "Offline-first repository pattern", "Hilt dependency injection overview"],
                        "practice_tasks": ["Create a Room DAO with Coroutines Flow return types", "Implement an offline-cache repository layer"],
                        "resources": [
                            {"title": "Save data in a local database using Room", "url": "https://developer.android.com/training/data-storage/room", "language": "English", "resource_type": "doc", "source": "Android Developers"}
                        ]
                    }
                ]
            }
        ]
    },
    "ios-swift": {
        "id": "ios-swift",
        "title": "iOS App Development (Swift & SwiftUI)",
        "category": "Mobile Development",
        "badge": "Apple Ecosystem",
        "description": "Master modern iOS application engineering with Swift 5+, SwiftUI, Combine, URLSession, and CoreData.",
        "skills": ["iOS", "Swift", "SwiftUI", "Combine", "URLSession", "CoreData", "MVVM", "Xcode"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Swift Language & SwiftUI Declarative UI",
                "description": "Foundations of Swift syntax, Optionals, Protocols, and building declarative views in SwiftUI.",
                "project_title": "Daily Habit & Streak Tracker iOS App",
                "project_description": "Build an iOS habit tracking app using SwiftUI Lists, NavigationStack, and custom SF Symbols.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Swift Language Fundamentals, Optionals & Protocols",
                        "learning_objective": "Master Swift type system: Optional unwrapping (if let, guard let), structs vs classes, and protocols.",
                        "subtopics": ["Optional binding and nil-coalescing", "Value types vs Reference types in Swift", "Protocol-oriented programming", "Closures and trailing syntax"],
                        "practice_tasks": ["Model a domain hierarchy using Protocols and Codable structs", "Handle deep optional parsing safely with guard let"],
                        "resources": [
                            {"title": "Swift.org Tour", "url": "https://docs.swift.org/swift-book/documentation/the-swift-programming-language/guidedtour/", "language": "English", "resource_type": "doc", "source": "Swift.org"},
                            {"title": "Swift & iOS in Telugu", "url": "https://www.youtube.com/results?search_query=swift+ios+tutorial+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "iOS App Development in Hindi", "url": "https://www.youtube.com/results?search_query=ios+development+course+in+hindi", "language": "Hindi", "resource_type": "video", "source": "YouTube Hindi"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "SwiftUI Declarative Views & State Management",
                        "learning_objective": "Build modern iOS views with @State, @Binding, @Observable, and NavigationStack.",
                        "subtopics": ["VStack, HStack, ZStack composition", "@State and @Binding bidirectional sync", "List with dynamic Identifiable items", "SF Symbols and Apple Human Interface Guidelines"],
                        "practice_tasks": ["Build a multi-step onboarding wizard in SwiftUI", "Implement a dark/light mode toggle with environment variables"],
                        "resources": [
                            {"title": "100 Days of SwiftUI", "url": "https://www.hackingwithswift.com/100/swiftui", "language": "English", "resource_type": "article", "source": "Hacking with Swift"}
                        ]
                    }
                ]
            },
            {
                "phase_number": 2,
                "title": "Networking, Persistence & Architecture",
                "description": "Connect SwiftUI apps to REST backends with async/await URLSession and persist data with CoreData / SwiftData.",
                "project_title": "Cryptocurrency Portfolio Watcher",
                "project_description": "Fetch live crypto prices asynchronously, store watchlist in SwiftData, and render dynamic charts.",
                "days": [
                    {
                        "day_number": 3,
                        "topic": "Async / Await Networking with URLSession",
                        "learning_objective": "Master Swift modern concurrency (async/await, Tasks, MainActor) and JSON decoding.",
                        "subtopics": ["URLSession.shared.data(from:)", "JSONDecoder with Codable protocols", "@MainActor UI dispatching", "Error handling with custom Swift enums"],
                        "practice_tasks": ["Fetch paginated JSON from a public API using async/await", "Handle network errors with a user-facing banner"],
                        "resources": [
                            {"title": "Swift Concurrency Guide", "url": "https://developer.apple.com/documentation/swift/concurrency", "language": "English", "resource_type": "doc", "source": "Apple Developer"}
                        ]
                    }
                ]
            }
        ]
    },
    "flutter-dart": {
        "id": "flutter-dart",
        "title": "Cross-Platform Mobile Dev (Flutter & Dart)",
        "category": "Mobile Development",
        "badge": "Multiplatform",
        "description": "Develop high-performance cross-platform iOS and Android apps from a single codebase with Flutter and Dart.",
        "skills": ["Flutter", "Dart", "Widgets", "Provider", "BLoC", "State Management", "REST APIs", "Dio"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Dart Language & Flutter Widget Architecture",
                "description": "Master Dart language concepts and core Flutter widget tree composition.",
                "project_title": "Campus Event Discovery & Ticket Booking App",
                "project_description": "Build an event catalog with card animations, category chips, and search filtering.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Dart Fundamentals & Flutter Widget Tree",
                        "learning_objective": "Master Dart OOP, sound null safety, and building Stateless vs Stateful widgets.",
                        "subtopics": ["Sound null safety in Dart", "StatelessWidget vs StatefulWidget lifecycle", "Container, Column, Row, Stack", "Scaffold, AppBar, FloatingActionButton"],
                        "practice_tasks": ["Create a responsive profile card with custom Avatar and Badge", "Implement dynamic counter with setState"],
                        "resources": [
                            {"title": "Dart Language Tour", "url": "https://dart.dev/guides/language/language-tour", "language": "English", "resource_type": "doc", "source": "Dart.dev"},
                            {"title": "Flutter Full Course in Telugu", "url": "https://www.youtube.com/results?search_query=flutter+full+course+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Flutter Full Course in Hindi", "url": "https://www.youtube.com/results?search_query=flutter+full+course+in+hindi", "language": "Hindi", "resource_type": "video", "source": "YouTube Hindi"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Navigation, State Management & Dio Networking",
                        "learning_objective": "Implement multi-screen routing, state management with Riverpod/Provider, and REST integration with Dio.",
                        "subtopics": ["Navigator 2.0 / GoRouter", "Provider and ChangeNotifier", "Dio HTTP client with interceptors", "JSON serialization with build_runner"],
                        "practice_tasks": ["Build a master-detail navigation flow passing typed arguments", "Fetch and render API data with a loading shimmer indicator"],
                        "resources": [
                            {"title": "Flutter Documentation", "url": "https://docs.flutter.dev/", "language": "English", "resource_type": "doc", "source": "Flutter.dev"}
                        ]
                    }
                ]
            }
        ]
    },
    "react-native": {
        "id": "react-native",
        "title": "Cross-Platform Mobile Dev (React Native & Expo)",
        "category": "Mobile Development",
        "badge": "Cross-Platform",
        "description": "Create native mobile apps using JavaScript, TypeScript, React Native, Expo, and native device bridge APIs.",
        "skills": ["React Native", "TypeScript", "Expo", "React Hooks", "AsyncStorage", "React Navigation", "Native Modules"],
        "phases": [
            {
                "phase_number": 1,
                "title": "React Native Fundamentals, Flexbox & Navigation",
                "description": "Build mobile user interfaces with React primitives, Flexbox styling, and React Navigation.",
                "project_title": "Food Delivery Ordering & Cart App",
                "project_description": "Build a React Native mobile application with restaurant listings, item cart, and checkout summary.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "React Native Core Components & Flexbox Layouts",
                        "learning_objective": "Learn React Native primitives (View, Text, Image, FlatList, TouchableOpacity) and mobile Flexbox.",
                        "subtopics": ["React Native architecture (Hermes engine, TurboModules)", "Flexbox in React Native (column default, alignItems, justifyContent)", "FlatList optimization (keyExtractor, getItemLayout)", "StyleSheet API"],
                        "practice_tasks": ["Build an Instagram-style post card with image, caption, and like button", "Render a virtualized list with FlatList"],
                        "resources": [
                            {"title": "React Native Official Docs", "url": "https://reactnative.dev/docs/getting-started", "language": "English", "resource_type": "doc", "source": "ReactNative.dev"},
                            {"title": "React Native in Telugu", "url": "https://www.youtube.com/results?search_query=react+native+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "React Native in Hindi", "url": "https://www.youtube.com/results?search_query=react+native+full+course+in+hindi", "language": "Hindi", "resource_type": "video", "source": "YouTube Hindi"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Navigation, Global State & Device Storage",
                        "learning_objective": "Master Stack and BottomTab navigation, state management with Zustand, and offline storage with AsyncStorage.",
                        "subtopics": ["@react-navigation/native-stack & bottom-tabs", "Zustand lightweight state store", "AsyncStorage key-value persistence", "Handling keyboard avoiding views"],
                        "practice_tasks": ["Set up bottom navigation with 3 tabs and persist user theme preference", "Build a shopping cart with Zustand store"],
                        "resources": [
                            {"title": "React Navigation Docs", "url": "https://reactnavigation.org/docs/getting-started", "language": "English", "resource_type": "doc", "source": "ReactNavigation"}
                        ]
                    }
                ]
            }
        ]
    },
    "deep-learning-pytorch": {
        "id": "deep-learning-pytorch",
        "title": "Deep Learning with PyTorch",
        "category": "AI & Data Science",
        "badge": "Deep Learning",
        "description": "Train and evaluate deep neural networks, CNNs, RNNs, and custom architectures using PyTorch and GPU acceleration.",
        "skills": ["PyTorch", "Deep Learning", "Tensors", "Neural Networks", "CNN", "RNN", "Loss Functions", "CUDA"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Tensors, Autograd & Multi-Layer Perceptrons",
                "description": "Understand tensor computation, automatic differentiation, loss functions, and backpropagation in PyTorch.",
                "project_title": "MNIST Digit & Fashion Classification Network",
                "project_description": "Implement a custom PyTorch MLP classifier achieving >97% accuracy on benchmark test sets.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "PyTorch Tensors, Autograd & Computation Graphs",
                        "learning_objective": "Master PyTorch tensor indexing, shapes, GPU allocation with .to(device), and autograd backward pass.",
                        "subtopics": ["Tensor shapes, slicing, reshaping (view vs reshape)", "requires_grad and computational graphs", "torch.cuda.is_available() and device agnostic code", "Dataset and DataLoader classes"],
                        "practice_tasks": ["Implement linear regression from scratch using pure PyTorch tensors and autograd", "Create a custom Dataset subclass with batch shuffling"],
                        "resources": [
                            {"title": "Deep Learning with PyTorch: A 60 Minute Blitz", "url": "https://pytorch.org/tutorials/beginner/deep_learning_60min_blitz.html", "language": "English", "resource_type": "doc", "source": "PyTorch.org"},
                            {"title": "PyTorch Deep Learning in Telugu", "url": "https://www.youtube.com/results?search_query=pytorch+deep+learning+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "PyTorch Full Course in Hindi", "url": "https://www.youtube.com/results?search_query=pytorch+full+course+in+hindi", "language": "Hindi", "resource_type": "video", "source": "YouTube Hindi"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Convolutional Neural Networks (CNN) & Transfer Learning",
                        "learning_objective": "Build and train CNNs with Conv2d, MaxPool2d, Dropout, BatchNorm, and fine-tune pre-trained torchvision ResNet.",
                        "subtopics": ["Convolution math (kernel size, stride, padding)", "Pooling and spatial dimension reduction", "Transfer learning with torchvision.models", "Model saving and checkpointing (torch.save)"],
                        "practice_tasks": ["Train a 3-layer CNN on CIFAR-10 with data augmentation", "Fine-tune a pre-trained ResNet18 on custom image classes"],
                        "resources": [
                            {"title": "Transfer Learning for Computer Vision Tutorial", "url": "https://pytorch.org/tutorials/beginner/transfer_learning_tutorial.html", "language": "English", "resource_type": "doc", "source": "PyTorch.org"}
                        ]
                    }
                ]
            }
        ]
    },
    "generative-ai-llms": {
        "id": "generative-ai-llms",
        "title": "Generative AI & Large Language Models (LLMs & RAG)",
        "category": "AI & Data Science",
        "badge": "Trending AI",
        "description": "Engineer production LLM applications using LangChain, Prompt Engineering, Vector Databases, and Retrieval-Augmented Generation (RAG).",
        "skills": ["Generative AI", "LLMs", "LangChain", "RAG", "Vector Databases", "ChromaDB", "Prompt Engineering", "OpenAI / Anthropic APIs"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Prompt Engineering, Function Calling & LangChain Foundations",
                "description": "Master structured prompting strategies, LLM APIs, LangChain chains, and JSON output parsing.",
                "project_title": "Automated Technical Resume Screener Agent",
                "project_description": "Build an LLM-powered candidate screening tool that evaluates resumes against job descriptions with structured JSON output.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Prompt Engineering & Structured Model Outputs",
                        "learning_objective": "Master system prompt design, Few-shot learning, temperature tuning, and guaranteed JSON schema output.",
                        "subtopics": ["Zero-shot vs Few-shot prompt engineering", "Temperature, top_p, and hallucinations mitigation", "Structured outputs via Pydantic schema validation", "Token budgeting and context window management"],
                        "practice_tasks": ["Write an extraction prompt that parses unstructured job postings into Pydantic models", "Benchmark prompt variations for hallucination reduction"],
                        "resources": [
                            {"title": "Prompt Engineering Guide", "url": "https://www.promptingguide.ai/", "language": "English", "resource_type": "article", "source": "DAIR.AI"},
                            {"title": "Generative AI in Telugu", "url": "https://www.youtube.com/results?search_query=generative+ai+course+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Generative AI & LLMs in Hindi", "url": "https://www.youtube.com/results?search_query=generative+ai+course+in+hindi", "language": "Hindi", "resource_type": "video", "source": "YouTube Hindi"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "RAG Pipelines with Embeddings & Vector Databases",
                        "learning_objective": "Construct end-to-end Retrieval-Augmented Generation pipelines using sentence embeddings and ChromaDB.",
                        "subtopics": ["Text chunking strategies (RecursiveCharacterTextSplitter)", "Dense embeddings (OpenAI, Hugging Face)", "Vector similarity search (Cosine, Dot Product)", "ChromaDB vector store collection management"],
                        "practice_tasks": ["Index a 50-page PDF document into ChromaDB", "Build a Q&A pipeline that cites specific source page references"],
                        "resources": [
                            {"title": "LangChain Official Documentation", "url": "https://python.langchain.com/docs/get_started/introduction", "language": "English", "resource_type": "doc", "source": "LangChain"}
                        ]
                    }
                ]
            }
        ]
    },
    "data-analytics": {
        "id": "data-analytics",
        "title": "Data Analytics & Visualization",
        "category": "AI & Data Science",
        "badge": "Business Intelligence",
        "description": "Extract actionable business insights through exploratory data analysis, statistical modeling, Pandas, and interactive dashboards.",
        "skills": ["Data Analytics", "Python", "Pandas", "NumPy", "Matplotlib", "Seaborn", "SQL", "Tableau"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Data Wrangling, Aggregations & Visual Storytelling",
                "description": "Clean messy datasets, compute statistical summaries with Pandas, and create compelling charts with Seaborn.",
                "project_title": "E-Commerce Customer Retention & Churn Analysis",
                "project_description": "Analyze 100K transaction records to detect customer churn drivers and present an executive visual report.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Pandas Data Cleaning, Filtering & GroupBy Aggregations",
                        "learning_objective": "Master DataFrame transformations: handling missing values, dtype casting, pivot tables, and groupby aggregations.",
                        "subtopics": ["Handling null values (dropna, fillna, interpolate)", "Multi-index and Pivot tables", "GroupBy with custom aggregate functions", "Merging and joining DataFrames"],
                        "practice_tasks": ["Clean a raw messy sales dataset with inconsistent dates and currencies", "Calculate 30-day rolling average revenue per customer segment"],
                        "resources": [
                            {"title": "Pandas User Guide", "url": "https://pandas.pydata.org/docs/user_guide/index.html", "language": "English", "resource_type": "doc", "source": "Pandas.pydata.org"},
                            {"title": "Data Analytics in Telugu", "url": "https://www.youtube.com/results?search_query=data+analytics+course+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Data Analysis with Python in Hindi", "url": "https://www.youtube.com/results?search_query=data+analysis+python+in+hindi", "language": "Hindi", "resource_type": "video", "source": "YouTube Hindi"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Exploratory Data Analysis (EDA) & Statistical Charts",
                        "learning_objective": "Create production-ready data visualizations using Seaborn and Matplotlib.",
                        "subtopics": ["Distribution plots (histplot, kdeplot)", "Correlation heatmaps", "Boxplots and outlier identification", "Data storytelling best practices"],
                        "practice_tasks": ["Generate an EDA dashboard with 4 coordinated charts and insight callouts", "Identify and treat outliers using the IQR method"],
                        "resources": [
                            {"title": "Seaborn Tutorial", "url": "https://seaborn.pydata.org/tutorial.html", "language": "English", "resource_type": "doc", "source": "Seaborn.pydata.org"}
                        ]
                    }
                ]
            }
        ]
    },
    "nlp-transformers": {
        "id": "nlp-transformers",
        "title": "Natural Language Processing & Transformers",
        "category": "AI & Data Science",
        "badge": "Applied NLP",
        "description": "Master NLP from TF-IDF tokenization to Hugging Face Transformers, BERT embeddings, and semantic search.",
        "skills": ["NLP", "Transformers", "BERT", "Hugging Face", "Tokenization", "TF-IDF", "Semantic Search", "Cosine Similarity"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Text Processing, Embeddings & Transformer Architectures",
                "description": "Learn classical text tokenization, vector space models, and self-attention mechanisms in Transformers.",
                "project_title": "Semantic Job Description to Resume Matcher",
                "project_description": "Author an NLP matching engine that scores resume relevance using pre-trained sentence transformer embeddings.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Tokenization, Stemming, TF-IDF & Cosine Similarity",
                        "learning_objective": "Understand NLP preprocessing pipelines and calculate semantic vector similarities.",
                        "subtopics": ["N-grams, stopword removal, and lemmatization", "TF-IDF vectorizer parameters (max_df, min_df)", "Cosine similarity math and sparse matrix representations", "Word2Vec and GloVe concepts"],
                        "practice_tasks": ["Build a TF-IDF text similarity calculator from scratch", "Extract top 10 keywords from technical job descriptions"],
                        "resources": [
                            {"title": "NLTK Book: Natural Language Processing with Python", "url": "https://www.nltk.org/book/", "language": "English", "resource_type": "article", "source": "NLTK.org"},
                            {"title": "NLP Tutorial in Telugu", "url": "https://www.youtube.com/results?search_query=nlp+tutorial+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "NLP Full Course in Hindi", "url": "https://www.youtube.com/results?search_query=nlp+course+in+hindi", "language": "Hindi", "resource_type": "video", "source": "YouTube Hindi"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Self-Attention, BERT & Hugging Face Pipelines",
                        "learning_objective": "Understand Transformer self-attention mechanisms and use Hugging Face for sentiment and classification.",
                        "subtopics": ["Scaled dot-product attention equation", "BERT bidirectional encoder representations", "Hugging Face pipeline() API", "Fine-tuning with Trainer API overview"],
                        "practice_tasks": ["Classify customer feedback sentiment using a Hugging Face pipeline", "Compute sentence embedding similarities using SentenceTransformers"],
                        "resources": [
                            {"title": "Hugging Face NLP Course", "url": "https://huggingface.co/learn/nlp-course", "language": "English", "resource_type": "doc", "source": "Hugging Face"}
                        ]
                    }
                ]
            }
        ]
    },
    "rust-lang": {
        "id": "rust-lang",
        "title": "Rust Systems Programming",
        "category": "Programming Languages",
        "badge": "Memory Safe",
        "description": "Build blazingly fast, memory-safe system software with Rust, ownership, borrowing, lifetimes, and fearless concurrency.",
        "skills": ["Rust", "Systems Programming", "Ownership", "Borrowing", "Lifetimes", "Cargo", "Concurrency", "Tokio"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Ownership, Borrowing, Lifetimes & Fearless Concurrency",
                "description": "Master the Rust compiler: zero-cost abstractions, borrow checker rules, and thread safety.",
                "project_title": "High-Throughput Multi-Threaded HTTP Proxy",
                "project_description": "Build a fast concurrent reverse proxy in Rust using Tokio and cross-thread message passing.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Rust Ownership, Borrowing & Lifetimes",
                        "learning_objective": "Understand how Rust enforces memory safety without a garbage collector through ownership rules.",
                        "subtopics": ["Ownership rules: move semantics vs Copy trait", "References and borrowing (& vs &mut)", "Slices and string types (String vs &str)", "Match pattern matching and Result<T, E> error handling"],
                        "practice_tasks": ["Write a command-line string parser with zero memory allocations", "Implement a custom binary tree with safe Box pointers"],
                        "resources": [
                            {"title": "The Rust Programming Language (The Book)", "url": "https://doc.rust-lang.org/book/", "language": "English", "resource_type": "doc", "source": "Rust-lang.org"},
                            {"title": "Rust Tutorial in Telugu", "url": "https://www.youtube.com/results?search_query=rust+programming+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "Rust Full Course in Hindi", "url": "https://www.youtube.com/results?search_query=rust+programming+course+in+hindi", "language": "Hindi", "resource_type": "video", "source": "YouTube Hindi"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Fearless Concurrency & Async Tokio",
                        "learning_objective": "Safely share state across threads using Arc, Mutex, and mpsc channels, and write async tasks with Tokio.",
                        "subtopics": ["std::thread and Send/Sync marker traits", "Arc<Mutex<T>> thread-safe state sharing", "mpsc channels for message passing", "Tokio runtime and async/await"],
                        "practice_tasks": ["Implement a thread-safe in-memory cache with reader-writer locks", "Author an async HTTP fetcher with Tokio and Reqwest"],
                        "resources": [
                            {"title": "Tokio Async Rust Tutorial", "url": "https://tokio.rs/tokio/tutorial", "language": "English", "resource_type": "doc", "source": "Tokio.rs"}
                        ]
                    }
                ]
            }
        ]
    },
    "typescript-lang": {
        "id": "typescript-lang",
        "title": "TypeScript & Modern Typed JavaScript",
        "category": "Programming Languages",
        "badge": "Web Standard",
        "description": "Level up JavaScript codebases with robust type safety, generics, utility types, and strict compiler configs.",
        "skills": ["TypeScript", "JavaScript", "Generics", "Type Inference", "Interfaces", "Union Types", "tsconfig"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Type System, Interfaces, Generics & Utility Types",
                "description": "Master advanced TypeScript features, union narrowing, generic constraints, and strict configuration.",
                "project_title": "Type-Safe REST API Client & Form Validation Library",
                "project_description": "Build a zero-dependency type-safe HTTP client with compile-time query and body inference.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Type Annotations, Interfaces & Discriminated Unions",
                        "learning_objective": "Master static typing in TypeScript, type aliases vs interfaces, and discriminated union patterns.",
                        "subtopics": ["Primitive and literal types", "Interfaces vs Type Aliases (declaration merging)", "Discriminated unions and exhaustive switch checks", "Type narrowing with typeof, instanceof, and custom type guards"],
                        "practice_tasks": ["Model a complex financial transaction state machine with discriminated unions", "Write custom type guards that validate external API responses"],
                        "resources": [
                            {"title": "TypeScript Handbook", "url": "https://www.typescriptlang.org/docs/handbook/intro.html", "language": "English", "resource_type": "doc", "source": "TypeScriptLang.org"},
                            {"title": "TypeScript in Telugu", "url": "https://www.youtube.com/results?search_query=typescript+tutorial+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "TypeScript Course in Hindi", "url": "https://www.youtube.com/results?search_query=typescript+course+in+hindi+chai+aur+code", "language": "Hindi", "resource_type": "video", "source": "Chai aur Code"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Generics, Utility Types & tsconfig Best Practices",
                        "learning_objective": "Write reusable generic code and leverage built-in utility types (Partial, Pick, Omit, Record).",
                        "subtopics": ["Generic functions, interfaces, and constraints (extends)", "Built-in utility types: Partial, Pick, Omit, Record, Readonly", "keyof and typeof operators", "Strict tsconfig configurations (noImplicitAny, strictNullChecks)"],
                        "practice_tasks": ["Implement a strongly typed EventEmitter with generic event payloads", "Create a deep Partial utility type for nested object updates"],
                        "resources": [
                            {"title": "Total TypeScript Tutorials", "url": "https://www.totaltypescript.com/tutorials", "language": "English", "resource_type": "article", "source": "Matt Pocock"}
                        ]
                    }
                ]
            }
        ]
    },
    "csharp-lang": {
        "id": "csharp-lang",
        "title": "C# & .NET Enterprise Development",
        "category": "Programming Languages",
        "badge": "Enterprise",
        "description": "Develop robust enterprise backends, web APIs, and desktop software with modern C# 12 and .NET 8.",
        "skills": ["C#", ".NET 8", "ASP.NET Core", "Entity Framework Core", "LINQ", "Dependency Injection", "REST APIs"],
        "phases": [
            {
                "phase_number": 1,
                "title": "C# OOP, LINQ & ASP.NET Core Web API",
                "description": "Master modern C# 12 features, LINQ data queries, Dependency Injection, and building REST APIs.",
                "project_title": "Enterprise Inventory & Order Management API",
                "project_description": "Build an ASP.NET Core Web API with Entity Framework Core, SQL Server/PostgreSQL, and JWT auth.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Modern C# Syntax, Records, Pattern Matching & LINQ",
                        "learning_objective": "Master C# language primitives, primary constructors, immutable records, pattern matching, and LINQ.",
                        "subtopics": ["Records vs Classes and value equality", "Pattern matching (switch expressions, property patterns)", "LINQ queries (Select, Where, OrderBy, GroupBy)", "Async/Await with Task and Task<T>"],
                        "practice_tasks": ["Write complex data aggregation queries using LINQ fluent syntax", "Model immutable domain events using C# records"],
                        "resources": [
                            {"title": "C# Documentation - Microsoft Learn", "url": "https://learn.microsoft.com/en-us/dotnet/csharp/", "language": "English", "resource_type": "doc", "source": "Microsoft Learn"},
                            {"title": "C# Full Course in Telugu", "url": "https://www.youtube.com/results?search_query=c%23+full+course+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "C# and .NET in Hindi", "url": "https://www.youtube.com/results?search_query=c%23+dotnet+course+in+hindi", "language": "Hindi", "resource_type": "video", "source": "YouTube Hindi"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "ASP.NET Core Minimal APIs & Entity Framework Core",
                        "learning_objective": "Build modern REST endpoints, configure Dependency Injection, and map relational databases with EF Core.",
                        "subtopics": ["Minimal APIs vs Controller-based APIs", "Dependency Injection lifetimes (Transient, Scoped, Singleton)", "EF Core DbContext, migrations, and LINQ-to-Entities", "Swagger / OpenAPI documentation"],
                        "practice_tasks": ["Create a CRUD REST service using ASP.NET Core Minimal APIs", "Perform database migrations with dotnet-ef CLI"],
                        "resources": [
                            {"title": "Tutorial: Create a minimal web API with ASP.NET Core", "url": "https://learn.microsoft.com/en-us/aspnet/core/tutorials/min-web-api", "language": "English", "resource_type": "doc", "source": "Microsoft Learn"}
                        ]
                    }
                ]
            }
        ]
    },
    "php-lang": {
        "id": "php-lang",
        "title": "Modern PHP 8 & Laravel Framework",
        "category": "Programming Languages",
        "badge": "Web Backend",
        "description": "Build secure, scalable full-stack web applications using modern PHP 8 features, Composer, and the Laravel framework.",
        "skills": ["PHP 8", "Laravel", "Composer", "Eloquent ORM", "MVC", "Blade", "MySQL", "Authentication"],
        "phases": [
            {
                "phase_number": 1,
                "title": "Modern PHP 8 Syntax & Laravel Architecture",
                "description": "Master PHP 8 constructor promotion, match expressions, Composer dependencies, and Laravel MVC architecture.",
                "project_title": "Multi-Tenant SaaS Helpdesk & Ticket Portal",
                "project_description": "Build a complete web application with Laravel, Blade templates, Eloquent relationships, and authentication.",
                "days": [
                    {
                        "day_number": 1,
                        "topic": "Modern PHP 8 Syntax, OOP & Composer",
                        "learning_objective": "Master PHP 8 typed properties, constructor property promotion, match expressions, and Composer autoloading.",
                        "subtopics": ["PHP 8 match expression vs switch", "Constructor property promotion and named arguments", "Namespaces, PSR-4 standards, and Composer", "Error handling with Throwable and custom exceptions"],
                        "practice_tasks": ["Build an autoloader package complying with PSR-4 standards", "Refactor legacy PHP code to modern PHP 8 type declarations"],
                        "resources": [
                            {"title": "PHP The Right Way", "url": "https://phptherightway.com/", "language": "English", "resource_type": "article", "source": "PHPTheRightWay"},
                            {"title": "PHP Full Course in Telugu", "url": "https://www.youtube.com/results?search_query=php+full+course+in+telugu", "language": "Telugu", "resource_type": "video", "source": "YouTube Telugu"},
                            {"title": "PHP Full Course in Hindi", "url": "https://www.youtube.com/results?search_query=php+tutorial+in+hindi", "language": "Hindi", "resource_type": "video", "source": "YouTube Hindi"}
                        ]
                    },
                    {
                        "day_number": 2,
                        "topic": "Laravel Routing, Controllers, Eloquent ORM & Blade",
                        "learning_objective": "Master Laravel request lifecycle, Eloquent relationships, migrations, and Blade templating.",
                        "subtopics": ["Artisan CLI commands", "Eloquent relationships (hasMany, belongsTo)", "Database migrations and seeders", "Blade components and layouts"],
                        "practice_tasks": ["Create a one-to-many relationship with Eloquent and write eager loading queries", "Build an authenticated admin panel with Laravel Breeze"],
                        "resources": [
                            {"title": "Laravel Documentation", "url": "https://laravel.com/docs", "language": "English", "resource_type": "doc", "source": "Laravel.com"}
                        ]
                    }
                ]
            }
        ]
    }

}

def get_all_catalog_courses() -> List[Dict[str, Any]]:
    """Returns summarized list of all available catalog courses for UI browsing and filtering."""
    results = []
    for c in CSE_COURSE_CATALOG.values():
        total_days = sum(len(phase["days"]) for phase in c["phases"])
        results.append({
            "id": c["id"],
            "title": c["title"],
            "category": c["category"],
            "badge": c.get("badge", "Popular"),
            "description": c["description"],
            "skills": c["skills"],
            "total_days": total_days,
            "total_phases": len(c["phases"])
        })
    return results


def get_course_details(course_id: str) -> Dict[str, Any]:
    """Returns the full phase and day hierarchy for a specific course."""
    return CSE_COURSE_CATALOG.get(course_id)
