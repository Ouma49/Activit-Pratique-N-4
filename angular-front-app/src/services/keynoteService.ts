const BASE = 'http://localhost:8081'; // gateway port

export async function listKeynotes() {
  const res = await fetch(`${BASE}/api/keynotes`);
  if (!res.ok) return [];
  return res.json();
}
