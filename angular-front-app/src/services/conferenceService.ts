const BASE = 'http://localhost:8081';

export async function listConferences() {
  const res = await fetch(`${BASE}/api/conferences`);
  if (!res.ok) return [];
  return res.json();
}
