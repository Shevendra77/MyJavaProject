const state = { questions: [], topics: [], activeTopic: 'all', search: '', status: 'all' };
const $ = (id) => document.getElementById(id);
let toastTimer;

async function api(path, options = {}) {
  const response = await fetch(path, { headers: { 'Content-Type': 'application/json' }, ...options });
  if (!response.ok) throw new Error(`Request failed (${response.status})`);
  return response.json();
}
function notify(message, error = false) {
  const toast = $('toast'); toast.textContent = message; toast.classList.toggle('error', error); toast.classList.add('show');
  clearTimeout(toastTimer); toastTimer = setTimeout(() => toast.classList.remove('show'), 2300);
}
function escapeHtml(value) { return String(value).replace(/[&<>"']/g, ch => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[ch])); }
function topicStats() {
  const stats = {};
  state.questions.forEach(q => { if (!stats[q.topic]) stats[q.topic] = { total: 0, solved: 0 }; stats[q.topic].total++; if (q.solved) stats[q.topic].solved++; });
  return stats;
}
function renderNavigation() {
  const stats = topicStats();
  $('topicNav').innerHTML = Object.entries(stats).map(([topic, s]) => { const percent = s.total ? Math.round(s.solved * 100 / s.total) : 0; return `<button class="topic-btn ${state.activeTopic === topic ? 'active' : ''}" data-topic-nav="${escapeHtml(topic)}"><span class="topic-label-wrap"><span class="topic-label-line"><span class="topic-dot"></span><span class="topic-name">${escapeHtml(topic)}</span></span><span class="topic-mini-track"><span style="width:${percent}%"></span></span></span><span class="topic-count">${s.solved}/${s.total}</span></button>`; }).join('');
  $('topicNav').querySelectorAll('[data-topic-nav]').forEach(btn => btn.addEventListener('click', () => setTopic(btn.dataset.topicNav)));
  $('topicFilter').innerHTML = '<option value="all">All topics</option>' + Object.keys(stats).map(topic => `<option value="${escapeHtml(topic)}">${escapeHtml(topic)}</option>`).join('');
  $('topicFilter').value = state.activeTopic;
  $('sideTotal').textContent = state.questions.length;
}
function renderDashboard() {
  const total = state.questions.length, solved = state.questions.filter(q => q.solved).length, remaining = total - solved;
  const percent = total ? Math.round(solved * 1000 / total) / 10 : 0;
  $('totalStat').textContent = total; $('solvedStat').textContent = solved; $('remainingStat').textContent = remaining;
  $('solvedPercent').textContent = `${percent}%`; $('progressPercent').textContent = `${percent}%`; $('overallBar').style.width = `${percent}%`;
  $('progressMessage').textContent = solved === 0 ? 'Your journey starts here.' : remaining === 0 ? 'Amazing! You completed every question.' : `${remaining} more to reach your goal.`;
}
function filteredQuestions() {
  return state.questions.filter(q => {
    const topicOk = state.activeTopic === 'all' || q.topic === state.activeTopic;
    const statusOk = state.status === 'all' || (state.status === 'solved' ? q.solved : !q.solved);
    const searchOk = q.title.toLowerCase().includes(state.search.toLowerCase()) || q.topic.toLowerCase().includes(state.search.toLowerCase());
    return topicOk && statusOk && searchOk;
  });
}
function renderQuestions() {
  const visible = filteredQuestions();
  $('questionRows').innerHTML = visible.map(q => `<tr class="${q.solved ? 'solved-row' : ''}"><td class="check-col"><input class="question-check" type="checkbox" aria-label="Mark ${escapeHtml(q.title)} solved" data-id="${q.id}" ${q.solved ? 'checked' : ''}></td><td><span class="question-title">${escapeHtml(q.title)}</span></td><td><span class="topic-pill" data-topic="${escapeHtml(q.topic)}">${escapeHtml(q.topic)}</span></td><td><span class="status-pill ${q.solved ? 'done' : ''}">${q.solved ? 'Completed' : 'Not solved'}</span></td></tr>`).join('');
  $('emptyState').classList.toggle('hidden', visible.length !== 0);
  $('questionRows').classList.toggle('hidden', visible.length === 0);
  $('visibleCount').textContent = `${visible.length} question${visible.length === 1 ? '' : 's'}`;
  $('footerCount').textContent = `Showing ${visible.length} of ${state.questions.length} questions`;
  $('questionRows').querySelectorAll('.question-check').forEach(input => input.addEventListener('change', () => updateQuestion(Number(input.dataset.id), input.checked, input)));
}
function renderAll() {
  const active = state.activeTopic;
  $('crumbTopic').textContent = active === 'all' ? 'All Questions' : active;
  $('listTitle').textContent = active === 'all' ? 'All Questions' : active;
  $('listSubtitle').textContent = active === 'all' ? 'Work through your placement checklist topic by topic.' : `Track your ${active} practice and keep moving forward.`;
  renderNavigation(); renderDashboard(); renderQuestions();
}
async function updateQuestion(id, solved, input) {
  const question = state.questions.find(q => q.id === id); if (!question) return;
  const old = question.solved; question.solved = solved; renderDashboard(); renderNavigation();
  // Keep the clicked checkbox in sync while the save request is in flight.
  input.disabled = true;
  try {
    const saved = await api(`/api/questions/${id}/status`, { method: 'PATCH', body: JSON.stringify({ solved }) });
    Object.assign(question, saved); renderAll(); notify(solved ? 'Question completed and saved ✓' : 'Question marked as not solved');
  } catch (e) {
    question.solved = old; renderAll(); notify('Could not save. Check that the server and MySQL are running.', true);
  }
}
function setTopic(topic) { state.activeTopic = topic; renderAll(); }
function exportCsv() {
  const rows = [['Question', 'Topic', 'Status'], ...state.questions.map(q => [q.title, q.topic, q.solved ? 'Completed' : 'Not solved'])];
  const csv = rows.map(row => row.map(value => `"${String(value).replace(/"/g, '""')}"`).join(',')).join('\r\n');
  const blob = new Blob(['\ufeff' + csv], { type: 'text/csv;charset=utf-8;' });
  const url = URL.createObjectURL(blob); const link = document.createElement('a');
  link.href = url; link.download = 'java-dsa-placement-progress.csv'; document.body.appendChild(link); link.click(); link.remove(); URL.revokeObjectURL(url);
  notify('Progress exported to CSV ✓');
}
function resetFilters() { state.activeTopic = 'all'; state.search = ''; state.status = 'all'; $('searchInput').value = ''; $('statusFilter').value = 'all'; renderAll(); notify('Filters cleared'); }
async function init() {
  try {
    state.questions = await api('/api/questions');
    $('allNav').addEventListener('click', () => setTopic('all'));
    $('searchInput').addEventListener('input', e => { state.search = e.target.value.trim(); renderQuestions(); });
    $('statusFilter').addEventListener('change', e => { state.status = e.target.value; renderQuestions(); });
    $('topicFilter').addEventListener('change', e => setTopic(e.target.value));
    $('exportButton').addEventListener('click', exportCsv);
    $('resetFilters').addEventListener('click', resetFilters);
    renderAll();
  } catch (e) {
    $('questionRows').innerHTML = '<tr><td colspan="4" class="loading">Could not load questions. Make sure Spring Boot and MySQL are running, then refresh.</td></tr>';
    notify('Could not connect to the backend.', true);
  }
}
init();
