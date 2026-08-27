# Universal Log Pre-processing Framework (ULPF)

> **Vendor-Agnostic, Lossless, Air-Gapped Ready Security Log Processing Engine**

ULPF is a high-throughput, plug-and-play log pre-processing framework designed to ingest raw events from perimeter network devices (Cisco, Fortinet, Palo Alto, Generic Syslog, JSON, CEF, LEEF), automatically detect log formats, preserve raw event payloads without information loss, and transform them into a unified taxonomy for SIEM, Data Lake, and AI/ML threat analytics.

---

## Key Features & Evaluation Alignment

- **Lossless Ingestion**: Original raw payloads are preserved with cryptographic hashes (`SHA-256`) in MongoDB for compliance and forensics.
- **Traceability Guarantee**: Every normalized event maintains a direct pointer (`rawLogId`) to its original raw log.
- **Plug-and-Play YAML Mapping**: Add new perimeter devices or custom application logs by simply dropping a YAML mapping file in `mappings/` without re-compiling source code.
- **Air-Gapped & Platform Independent**: Completely containerizable with Docker / Docker Compose and deployable in offline/air-gapped security networks.
- **AI/ML Ready Schema**: Standardized JSON taxonomy with normalized timestamps, IP fields, severity scores, and extra key-value attributes.

---

## System Architecture Overview

```
                      [ Multi-Vendor Log Sources ]
            (Cisco ASA, Fortigate, Palo Alto, Generic Syslog)
                                   │
                                   ▼
              ┌──────────────────────────────────────────┐
              │     Ingestion Engine (REST / Syslog)     │
              └────────────────────┬─────────────────────┘
                                   │
                                   ▼
              ┌──────────────────────────────────────────┐
              │      Format Detection Engine (Regex)     │
              └────────────────────┬─────────────────────┘
                                   │
                                   ▼
              ┌──────────────────────────────────────────┐
              │    YAML Normalization & Mapping Engine   │
              │  (cisco.yml, fortinet.yml, paloalto.yml) │
              └────────────────────┬─────────────────────┘
                                   │
                                   ▼
              ┌──────────────────────────────────────────┐
              │      MongoDB Multi-Collection Storage    │
              │ (raw_logs, normalized_events, errors)    │
              └────────────────────┬─────────────────────┘
                                   │
                                   ▼
              ┌──────────────────────────────────────────┐
              │  REST Query APIs & React Dashboard UI    │
              └──────────────────────────────────────────┘
```

---

## Quick Setup & Deployment Guide

### Prerequisites
- **Java 17+**
- **Node.js 18+ & npm**
- **MongoDB** (Local instance or MongoDB Atlas)

---

### Step 1: Start Backend Service (Spring Boot)

```bash
cd backend-java

# Set Java 17 (if using SDKMAN)
source ~/.sdkman/bin/sdkman-init.sh
sdk use java 17.0.20-tem

# Run the backend server
./mvnw spring-boot:run
```
*Backend runs on `http://localhost:8080` (Swagger docs available at `http://localhost:8080/swagger-ui.html`)*

---

### Step 2: Start Frontend Dashboard (React + TypeScript)

```bash
cd frontend-react

# Install dependencies
npm install

# Start Vite dev server
npm run dev
```
*Frontend runs on `http://localhost:5173`*

---

## API Endpoints Reference

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/v1/ingest/text` | Ingest raw log entry with automatic vendor detection & normalization |
| `POST` | `/api/v1/events/search` | Query normalized events with multi-field filters & pagination |
| `GET` | `/api/v1/stats/dashboard` | Retrieve live system metrics, vendor breakdowns, and success rates |
| `GET` | `/api/v1/events/export` | Export normalized events in JSON/JSONL format for SIEM/Data Lakes |
| `POST` | `/api/v1/auth/login` | JWT Authentication endpoint |

---

## Air-Gapped & Docker Deployment

To build container images for air-gapped security network deployments:

```bash
cd backend-java
docker build -t ulpf-backend:1.0 .
```

---

## Adding a New Vendor Parser (Plug-and-Play)

Create a new `.yml` file under `backend-java/src/main/resources/mappings/custom_vendor.yml`:

```yaml
vendor: CUSTOM_DEVICE
description: Custom Perimeter Firewall Log Mapping
fieldMappings:
  - sourceField: src_ip
    targetField: sourceIp
  - sourceField: dst_ip
    targetField: destinationIp
  - sourceField: action
    targetField: action
severityMapping:
  CRITICAL: 1
  HIGH: 2
  MEDIUM: 3
```

Restart or reload the application; the framework will automatically register and apply the new parser!
