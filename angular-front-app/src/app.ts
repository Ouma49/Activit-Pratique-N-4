import { listKeynotes } from './services/keynoteService';
import { listConferences } from './services/conferenceService';

function el(tag: string, text?: string) {
  const e = document.createElement(tag);
  if (text) e.textContent = text;
  return e;
}

export async function renderApp(container: HTMLElement | null) {
  if (!container) return;
  container.innerHTML = '';

  const header = el('h1', 'Activite4 - Front (minimal)');
  const btnKeynotes = el('button', 'Load Keynotes');
  const btnConfs = el('button', 'Load Conferences');
  const out = el('div');

  btnKeynotes.addEventListener('click', async () => {
    out.innerHTML = '<h2>Keynotes</h2>';
    const ks = await listKeynotes();
    const ul = el('ul');
    ks.forEach((k: any) => {
      const li = el('li', `${k.nom} ${k.prenom} — ${k.fonction} (${k.email})`);
      ul.appendChild(li);
    });
    out.appendChild(ul);
  });

  btnConfs.addEventListener('click', async () => {
    out.innerHTML = '<h2>Conferences</h2>';
    const cs = await listConferences();
    const ul = el('ul');
    cs.forEach((c: any) => {
      const li = el('li', `${c.titre} — ${c.type} — score: ${c.score || 'n/a'}`);
      ul.appendChild(li);
    });
    out.appendChild(ul);
  });

  container.appendChild(header);
  container.appendChild(btnKeynotes);
  container.appendChild(btnConfs);
  container.appendChild(out);
}
