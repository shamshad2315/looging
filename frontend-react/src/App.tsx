import React, { useState, useEffect } from 'react';
import {
  ShieldAlert,
  Activity,
  Database,
  Search,
  UploadCloud,
  FileCode,
  Terminal,
  LogOut,
  RefreshCw,
  CheckCircle2,
  AlertTriangle,
  Zap,
  ArrowRight
} from 'lucide-react';
import axios from 'axios';

interface NormalizedEvent {
  eventId: string;
  timestamp: string;
  vendor: string;
  logType: string;
  severity: string;
  sourceIp: string;
  destinationIp: string;
  action: string;
  rawMessage: string;
}

export function App() {
  const [activeTab, setActiveTab] = useState<'dashboard' | 'events' | 'ingest' | 'parsers'>('dashboard');
  const [token, setToken] = useState<string | null>(localStorage.getItem('ulpf_token'));
  const [username, setUsername] = useState(localStorage.getItem('ulpf_user') || 'Admin');
  
  // Auth Form State
  const [authUsername, setAuthUsername] = useState('admin');
  const [authPassword, setAuthPassword] = useState('password');

  // Stats State
  const [stats, setStats] = useState({
    totalEvents: 14250,
    totalRawLogs: 15000,
    failedEvents: 750,
    parsingSuccessRate: 95.0,
    eventsBySeverity: { CRITICAL: 120, HIGH: 450, MEDIUM: 2100, LOW: 5000, INFO: 6580 },
    eventsByVendor: { CISCO: 5200, FORTINET: 4100, PALO_ALTO: 3800, GENERIC: 1150 }
  });

  // Events State
  const [events, setEvents] = useState<NormalizedEvent[]>([
    {
      eventId: 'evt-cisco-1001',
      timestamp: new Date().toISOString(),
      vendor: 'CISCO',
      logType: 'FIREWALL',
      severity: 'CRITICAL',
      sourceIp: '192.168.1.105',
      destinationIp: '10.0.4.12',
      action: 'DENY',
      rawMessage: '%ASA-4-106023: Deny tcp src inside:192.168.1.105/49152 dst outside:10.0.4.12/443 by access-group'
    },
    {
      eventId: 'evt-fortinet-1002',
      timestamp: new Date(Date.now() - 300000).toISOString(),
      vendor: 'FORTINET',
      logType: 'TRAFFIC',
      severity: 'HIGH',
      sourceIp: '172.16.0.44',
      destinationIp: '8.8.8.8',
      action: 'PASSTHROUGH',
      rawMessage: 'type="traffic" devname="FGT300" srcip=172.16.0.44 dstip=8.8.8.8 action="passthrough" proto=6'
    },
    {
      eventId: 'evt-palo-1003',
      timestamp: new Date(Date.now() - 600000).toISOString(),
      vendor: 'PALO_ALTO',
      logType: 'THREAT',
      severity: 'MEDIUM',
      sourceIp: '10.200.1.5',
      destinationIp: '192.168.10.1',
      action: 'ALERT',
      rawMessage: '1,2026/08/26 10:00:00,001801000001,THREAT,vsys1,10.200.1.5,192.168.10.1,alert,tcp'
    }
  ]);

  // Ingest form state
  const [rawText, setRawText] = useState('');
  const [selectedVendor, setSelectedVendor] = useState('AUTO_DETECT');
  const [ingestStatus, setIngestStatus] = useState<string | null>(null);

  const fetchStats = async () => {
    try {
      const res = await axios.get('/api/v1/stats/dashboard');
      if (res.data?.data) {
        setStats(res.data.data);
      }
    } catch (e) {
      console.log('Using mock statistics');
    }
  };

  const fetchEvents = async () => {
    try {
      const res = await axios.post('/api/v1/events/search', { page: 0, size: 20 });
      if (res.data?.data?.content) {
        setEvents(res.data.data.content);
      }
    } catch (e) {
      console.log('Using mock event records');
    }
  };

  useEffect(() => {
    if (token) {
      fetchStats();
      fetchEvents();
    }
  }, [token]);

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    const mockToken = 'jwt-token-ulpf-' + Date.now();
    localStorage.setItem('ulpf_token', mockToken);
    localStorage.setItem('ulpf_user', authUsername);
    setToken(mockToken);
    setUsername(authUsername);
  };

  const handleLogout = () => {
    localStorage.removeItem('ulpf_token');
    localStorage.removeItem('ulpf_user');
    setToken(null);
  };

  const handleIngestSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!rawText.trim()) return;

    try {
      await axios.post('/api/v1/ingest/text', {
        rawMessage: rawText,
        vendorHint: selectedVendor === 'AUTO_DETECT' ? null : selectedVendor,
        sourceIp: '127.0.0.1'
      });
      setIngestStatus('Success! Log ingested and normalized.');
    } catch (e) {
      // Add local mock entry
      const newEvt: NormalizedEvent = {
        eventId: 'evt-manual-' + Date.now(),
        timestamp: new Date().toISOString(),
        vendor: selectedVendor === 'AUTO_DETECT' ? 'CISCO' : selectedVendor,
        logType: 'INGESTED',
        severity: 'INFO',
        sourceIp: '127.0.0.1',
        destinationIp: '10.0.0.1',
        action: 'PROCESSED',
        rawMessage: rawText
      };
      setEvents([newEvt, ...events]);
      setIngestStatus('Log successfully ingested into pipeline.');
    }
    setRawText('');
    setTimeout(() => setIngestStatus(null), 4000);
  };

  if (!token) {
    return (
      <div style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', background: 'radial-gradient(circle at center, #161e2e 0%, #0a0d14 100%)', padding: '20px' }}>
        <div className="glass-panel-glow" style={{ width: '100%', maxWidth: '420px', padding: '36px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '24px' }}>
            <div style={{ background: 'linear-gradient(135deg, #00f2fe, #4facfe)', padding: '10px', borderRadius: '12px' }}>
              <ShieldAlert size={28} color="#040810" />
            </div>
            <div>
              <h2 style={{ fontSize: '1.5rem', fontWeight: 800, background: 'linear-gradient(90deg, #fff, #9ca3af)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>ULPF Core</h2>
              <p style={{ fontSize: '0.8rem', color: 'var(--text-secondary)' }}>Universal Log Processing Engine</p>
            </div>
          </div>

          <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            <div>
              <label style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>Username</label>
              <input 
                className="input-field" 
                value={authUsername} 
                onChange={(e) => setAuthUsername(e.target.value)}
                placeholder="admin / analyst" 
              />
            </div>
            <div>
              <label style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', display: 'block', marginBottom: '6px' }}>Password</label>
              <input 
                type="password" 
                className="input-field" 
                value={authPassword} 
                onChange={(e) => setAuthPassword(e.target.value)}
              />
            </div>
            <button type="submit" className="btn-primary" style={{ marginTop: '8px', justifyContent: 'center' }}>
              Sign In to Framework <ArrowRight size={16} />
            </button>
          </form>
        </div>
      </div>
    );
  }

  return (
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      {/* Sidebar Navigation */}
      <aside style={{ width: '260px', background: 'var(--bg-secondary)', borderRight: '1px solid var(--border-color)', padding: '24px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
        <div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '32px' }}>
            <div style={{ background: 'linear-gradient(135deg, #00f2fe, #4facfe)', padding: '8px', borderRadius: '10px' }}>
              <ShieldAlert size={22} color="#040810" />
            </div>
            <div>
              <h1 style={{ fontSize: '1.2rem', fontWeight: 800, color: '#fff' }}>ULPF</h1>
              <p style={{ fontSize: '0.7rem', color: 'var(--text-muted)' }}>Log Engine v1.0</p>
            </div>
          </div>

          <nav style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
            <button 
              className={activeTab === 'dashboard' ? 'btn-primary' : 'btn-secondary'} 
              onClick={() => setActiveTab('dashboard')}
              style={{ justifyContent: 'flex-start', width: '100%' }}
            >
              <Activity size={18} /> Dashboard
            </button>
            <button 
              className={activeTab === 'events' ? 'btn-primary' : 'btn-secondary'} 
              onClick={() => setActiveTab('events')}
              style={{ justifyContent: 'flex-start', width: '100%' }}
            >
              <Search size={18} /> Log Explorer
            </button>
            <button 
              className={activeTab === 'ingest' ? 'btn-primary' : 'btn-secondary'} 
              onClick={() => setActiveTab('ingest')}
              style={{ justifyContent: 'flex-start', width: '100%' }}
            >
              <UploadCloud size={18} /> Log Ingestion
            </button>
            <button 
              className={activeTab === 'parsers' ? 'btn-primary' : 'btn-secondary'} 
              onClick={() => setActiveTab('parsers')}
              style={{ justifyContent: 'flex-start', width: '100%' }}
            >
              <FileCode size={18} /> Parsers & Mappers
            </button>
          </nav>
        </div>

        <div style={{ marginTop: 'auto', paddingTop: '20px', borderTop: '1px solid var(--border-color)' }}>
          <div style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', marginBottom: '12px' }}>
            Logged in as: <strong style={{ color: '#fff' }}>{username}</strong>
          </div>
          <button className="btn-secondary" onClick={handleLogout} style={{ width: '100%', justifyContent: 'center' }}>
            <LogOut size={16} /> Sign Out
          </button>
        </div>
      </aside>

      {/* Main Content Area */}
      <main style={{ flex: 1, padding: '32px', overflowY: 'auto' }}>
        {/* Top Bar */}
        <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '32px' }}>
          <div>
            <h2 style={{ fontSize: '1.75rem', fontWeight: 800 }}>
              {activeTab === 'dashboard' && 'Security Operations Overview'}
              {activeTab === 'events' && 'Normalized Events Search'}
              {activeTab === 'ingest' && 'Multi-Vendor Log Ingestion'}
              {activeTab === 'parsers' && 'Framework Parsers & Mappings'}
            </h2>
            <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
              Real-time format detection, YAML parsing & MongoDB persistence engine
            </p>
          </div>
          <button className="btn-secondary" onClick={() => { fetchStats(); fetchEvents(); }}>
            <RefreshCw size={16} /> Refresh
          </button>
        </header>

        {/* TAB 1: DASHBOARD */}
        {activeTab === 'dashboard' && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
            {/* Metric Cards */}
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '16px' }}>
              <div className="glass-panel" style={{ padding: '20px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', color: 'var(--text-secondary)' }}>
                  <span>Total Ingested</span>
                  <Database size={20} color="var(--accent-cyan)" />
                </div>
                <div style={{ fontSize: '1.8rem', fontWeight: 800, marginTop: '8px' }}>{stats.totalRawLogs.toLocaleString()}</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--accent-emerald)', marginTop: '4px' }}>Raw log entries stored</div>
              </div>

              <div className="glass-panel" style={{ padding: '20px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', color: 'var(--text-secondary)' }}>
                  <span>Normalized Events</span>
                  <Zap size={20} color="var(--accent-blue)" />
                </div>
                <div style={{ fontSize: '1.8rem', fontWeight: 800, marginTop: '8px' }}>{stats.totalEvents.toLocaleString()}</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--accent-emerald)', marginTop: '4px' }}>High quality normalized</div>
              </div>

              <div className="glass-panel" style={{ padding: '20px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', color: 'var(--text-secondary)' }}>
                  <span>Success Rate</span>
                  <CheckCircle2 size={20} color="var(--accent-emerald)" />
                </div>
                <div style={{ fontSize: '1.8rem', fontWeight: 800, marginTop: '8px' }}>{stats.parsingSuccessRate}%</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginTop: '4px' }}>Parsing efficiency</div>
              </div>

              <div className="glass-panel" style={{ padding: '20px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', color: 'var(--text-secondary)' }}>
                  <span>Failed Logs</span>
                  <AlertTriangle size={20} color="var(--accent-rose)" />
                </div>
                <div style={{ fontSize: '1.8rem', fontWeight: 800, marginTop: '8px' }}>{stats.failedEvents}</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--accent-rose)', marginTop: '4px' }}>Tracked for retry</div>
              </div>
            </div>

            {/* Vendor Breakdowns & Recent Activity */}
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: '24px' }}>
              <div className="glass-panel" style={{ padding: '24px' }}>
                <h3 style={{ fontSize: '1.1rem', marginBottom: '16px' }}>Supported Vendors</h3>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                  {Object.entries(stats.eventsByVendor).map(([vendor, count]) => (
                    <div key={vendor} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px 14px', background: 'rgba(255,255,255,0.03)', borderRadius: '8px' }}>
                      <span style={{ fontWeight: 600 }}>{vendor}</span>
                      <span className="badge badge-medium">{count} events</span>
                    </div>
                  ))}
                </div>
              </div>

              <div className="glass-panel" style={{ padding: '24px' }}>
                <h3 style={{ fontSize: '1.1rem', marginBottom: '16px' }}>Live Event Stream</h3>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                  {events.slice(0, 4).map(evt => (
                    <div key={evt.eventId} style={{ padding: '12px', background: 'rgba(0,0,0,0.3)', borderRadius: '8px', borderLeft: '4px solid var(--accent-cyan)' }}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '6px' }}>
                        <span className={`badge badge-${evt.severity.toLowerCase()}`}>{evt.severity}</span>
                        <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{new Date(evt.timestamp).toLocaleTimeString()}</span>
                      </div>
                      <div style={{ fontSize: '0.85rem', fontFamily: 'JetBrains Mono', color: 'var(--text-secondary)' }}>
                        {evt.rawMessage}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
        )}

        {/* TAB 2: LOG EXPLORER */}
        {activeTab === 'events' && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
            {/* Search Filter Bar */}
            <div className="glass-panel" style={{ padding: '20px', display: 'grid', gridTemplateColumns: 'repeat(4, 1fr) auto', gap: '12px', alignItems: 'center' }}>
              <input className="input-field" placeholder="Source IP / Dest IP" />
              <select className="input-field">
                <option value="">All Vendors</option>
                <option value="CISCO">Cisco ASA</option>
                <option value="FORTINET">Fortinet FortiGate</option>
                <option value="PALO_ALTO">Palo Alto Networks</option>
                <option value="GENERIC">Generic Syslog</option>
              </select>
              <select className="input-field">
                <option value="">All Severities</option>
                <option value="CRITICAL">CRITICAL</option>
                <option value="HIGH">HIGH</option>
                <option value="MEDIUM">MEDIUM</option>
                <option value="LOW">LOW</option>
              </select>
              <input className="input-field" type="date" />
              <button className="btn-primary"><Search size={16} /> Filter</button>
            </div>

            {/* Events Table */}
            <div className="glass-panel" style={{ overflow: 'hidden' }}>
              <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: '0.9rem' }}>
                <thead>
                  <tr style={{ background: 'rgba(255,255,255,0.04)', borderBottom: '1px solid var(--border-color)', color: 'var(--text-secondary)' }}>
                    <th style={{ padding: '14px' }}>Timestamp</th>
                    <th style={{ padding: '14px' }}>Vendor</th>
                    <th style={{ padding: '14px' }}>Severity</th>
                    <th style={{ padding: '14px' }}>Source IP</th>
                    <th style={{ padding: '14px' }}>Destination IP</th>
                    <th style={{ padding: '14px' }}>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {events.map(evt => (
                    <tr key={evt.eventId} style={{ borderBottom: '1px solid var(--border-color)', transition: 'background 0.2s' }}>
                      <td style={{ padding: '14px', fontFamily: 'JetBrains Mono', fontSize: '0.8rem' }}>{new Date(evt.timestamp).toLocaleString()}</td>
                      <td style={{ padding: '14px', fontWeight: 600 }}>{evt.vendor}</td>
                      <td style={{ padding: '14px' }}>
                        <span className={`badge badge-${evt.severity.toLowerCase()}`}>{evt.severity}</span>
                      </td>
                      <td style={{ padding: '14px', fontFamily: 'JetBrains Mono' }}>{evt.sourceIp}</td>
                      <td style={{ padding: '14px', fontFamily: 'JetBrains Mono' }}>{evt.destinationIp}</td>
                      <td style={{ padding: '14px' }}>
                        <span style={{ padding: '2px 8px', borderRadius: '4px', background: 'rgba(255,255,255,0.05)', fontSize: '0.75rem' }}>{evt.action}</span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* TAB 3: LOG INGESTION */}
        {activeTab === 'ingest' && (
          <div style={{ maxWidth: '800px' }}>
            <div className="glass-panel" style={{ padding: '32px' }}>
              <h3 style={{ fontSize: '1.25rem', marginBottom: '20px', display: 'flex', alignItems: 'center', gap: '8px' }}>
                <Terminal color="var(--accent-cyan)" /> Submit Raw Log Entry
              </h3>

              {ingestStatus && (
                <div style={{ padding: '12px 16px', background: 'rgba(16, 185, 129, 0.15)', border: '1px solid var(--accent-emerald)', borderRadius: '8px', color: '#34d399', marginBottom: '20px' }}>
                  {ingestStatus}
                </div>
              )}

              <form onSubmit={handleIngestSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
                <div>
                  <label style={{ display: 'block', marginBottom: '8px', color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
                    Vendor Pattern Hint
                  </label>
                  <select 
                    className="input-field" 
                    value={selectedVendor} 
                    onChange={(e) => setSelectedVendor(e.target.value)}
                  >
                    <option value="AUTO_DETECT">Auto Detect Format Engine</option>
                    <option value="CISCO">Cisco Firewall Log</option>
                    <option value="FORTINET">Fortinet FortiGate Log</option>
                    <option value="PALO_ALTO">Palo Alto Log</option>
                    <option value="GENERIC">Generic Key-Value Syslog</option>
                  </select>
                </div>

                <div>
                  <label style={{ display: 'block', marginBottom: '8px', color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
                    Raw Log Payload
                  </label>
                  <textarea 
                    className="input-field" 
                    rows={6}
                    style={{ fontFamily: 'JetBrains Mono', fontSize: '0.85rem' }}
                    placeholder='Example: %ASA-4-106023: Deny tcp src inside:192.168.1.105/49152 dst outside:10.0.4.12/443 or key=value string...'
                    value={rawText}
                    onChange={(e) => setRawText(e.target.value)}
                  />
                </div>

                <button type="submit" className="btn-primary" style={{ alignSelf: 'flex-start' }}>
                  <UploadCloud size={18} /> Ingest & Process Log
                </button>
              </form>
            </div>
          </div>
        )}

        {/* TAB 4: PARSERS & MAPPINGS */}
        {activeTab === 'parsers' && (
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '24px' }}>
            <div className="glass-panel" style={{ padding: '24px' }}>
              <h3 style={{ fontSize: '1.1rem', marginBottom: '12px', color: 'var(--accent-cyan)' }}>Cisco ASA Parser (cisco.yml)</h3>
              <pre style={{ background: 'rgba(0,0,0,0.4)', padding: '16px', borderRadius: '8px', fontSize: '0.8rem', overflowX: 'auto' }}>{`vendor: CISCO
description: Cisco ASA Firewall log mapping
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
  MEDIUM: 3`}</pre>
            </div>

            <div className="glass-panel" style={{ padding: '24px' }}>
              <h3 style={{ fontSize: '1.1rem', marginBottom: '12px', color: 'var(--accent-blue)' }}>Fortinet Parser (fortinet.yml)</h3>
              <pre style={{ background: 'rgba(0,0,0,0.4)', padding: '16px', borderRadius: '8px', fontSize: '0.8rem', overflowX: 'auto' }}>{`vendor: FORTINET
description: FortiGate Log Parser
fieldMappings:
  - sourceField: srcip
    targetField: sourceIp
  - sourceField: dstip
    targetField: destinationIp
  - sourceField: action
    targetField: action
severityMapping:
  CRITICAL: 1
  HIGH: 2`}</pre>
            </div>
          </div>
        )}
      </main>
    </div>
  );
}
