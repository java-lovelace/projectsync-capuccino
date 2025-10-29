

// Basic Frontend JS to consume /api/projects endpoints
// Endpoints:
// - GET    /api/projects
// - GET    /api/projects/{id}
// - POST   /api/projects
// - PATCH  /api/projects/{id}
// - DELETE /api/projects/{id}

const API_BASE = 'http://localhost:8090/api/projects';

// Elements
const tbody = document.getElementById('projects-tbody');
const emptyState = document.getElementById('empty-state');
const btnRefresh = document.getElementById('btn-refresh');
const btnNew = document.getElementById('btn-new');
const loadingIndicator = document.getElementById('loading-indicator');
const toast = document.getElementById('toast');

// Form elements
const formSection = document.getElementById('form-section');
const form = document.getElementById('project-form');
const formTitle = document.getElementById('form-title');
const inputId = document.getElementById('project-id');
const inputName = document.getElementById('name');
const inputDescription = document.getElementById('description');
const inputStatus = document.getElementById('status');
const inputResponsible = document.getElementById('responsible');
const btnCancel = document.getElementById('btn-cancel');

function showLoading(show) {
  if (!loadingIndicator) return;
  loadingIndicator.classList.toggle('hidden', !show);
}

function showToast(message, type = 'info') {
  if (!toast) return;
  toast.textContent = message;
  toast.style.background = type === 'error' ? '#dc2626' : (type === 'success' ? '#16a34a' : '#111827');
  toast.classList.remove('hidden');
  setTimeout(() => toast.classList.add('hidden'), 2600);
}

function resetForm() {
  inputId.value = '';
  inputName.value = '';
  inputDescription.value = '';
  inputStatus.value = '';
  inputResponsible.value = '';
}

function openForm(mode = 'create', data = null) {
  formSection.classList.remove('hidden');
  if (mode === 'edit' && data) {
    formTitle.textContent = 'Edit project';
    inputId.value = data.id;
    inputName.value = data.name ?? '';
    inputDescription.value = data.description ?? '';
    inputStatus.value = data.status ?? '';
    inputResponsible.value = data.responsible ?? '';
  } else {
    formTitle.textContent = 'New project';
    resetForm();
  }
  inputName.focus();
}

function closeForm() {
  formSection.classList.add('hidden');
  resetForm();
}

function projectRowTemplate(p) {
  const safe = (v) => v == null ? '' : String(v);
  return `
    <tr>
      <td class="px-4 py-2 text-sm text-gray-600">${safe(p.id)}</td>
      <td class="px-4 py-2">${safe(p.name)}</td>
      <td class="px-4 py-2 text-sm text-gray-600">${safe(p.description)}</td>
      <td class="px-4 py-2"><span class="inline-flex items-center rounded-md bg-gray-100 px-2 py-1 text-xs font-medium text-gray-700">${safe(p.status)}</span></td>
      <td class="px-4 py-2">${safe(p.responsible)}</td>
      <td class="px-4 py-2">
        <div class="flex items-center gap-2">
          <button class="btn btn-ghost" data-action="edit" data-id="${p.id}">Edit</button>
          <button class="btn btn-danger" data-action="delete" data-id="${p.id}">Delete</button>
        </div>
      </td>
    </tr>
  `;
}

async function fetchJSON(url, options = {}) {
  const resp = await fetch(url, options);
  if (!resp.ok) {
    // Try to decode JSON error
    let details = '';
    try {
      const err = await resp.json();
      details = err.error || err.message || JSON.stringify(err);
    } catch (e) {
      details = await resp.text();
    }
    const msg = `Error ${resp.status}: ${details}`;
    throw new Error(msg);
  }
  const text = await resp.text();
  return text ? JSON.parse(text) : null;
}

async function loadProjects() {
  showLoading(true);
  try {
    const data = await fetchJSON(API_BASE);
    renderProjects(data);
  } catch (e) {
    console.error(e);
    showToast(e.message || 'Error cargando projects', 'error');
  } finally {
    showLoading(false);
  }
}

function renderProjects(projects) {
  tbody.innerHTML = '';
  if (!projects || projects.length === 0) {
    emptyState.classList.remove('hidden');
    return;
  }
  emptyState.classList.add('hidden');
  const rows = projects.map(projectRowTemplate).join('');
  tbody.innerHTML = rows;
}

async function createProject(payload) {
  return fetchJSON(API_BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  });
}

async function updateProject(id, payload) {
  return fetchJSON(`${API_BASE}/${id}`, {
    method: 'PATCH',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  });
}

async function deleteProject(id) {
  const resp = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
  if (!resp.ok && resp.status !== 204) {
    let text = await resp.text();
    throw new Error(text || `Error ${resp.status} al delete`);
  }
  return true;
}

// Event Listeners
btnRefresh?.addEventListener('click', () => loadProjects());
btnNew?.addEventListener('click', () => openForm('create'));
btnCancel?.addEventListener('click', () => closeForm());

// Delegate edit/delete buttons on table
tbody?.addEventListener('click', async (e) => {
  const target = e.target.closest('button');
  if (!target) return;
  const id = target.getAttribute('data-id');
  const action = target.getAttribute('data-action');
  if (!id || !action) return;

  if (action === 'edit') {
    // Find current row values to prefill
    const row = target.closest('tr');
    const values = Array.from(row.querySelectorAll('td')).map(td => td.textContent.trim());
    const data = {
      id: Number(values[0]),
      name: values[1],
      description: values[2],
      status: values[3],
      responsible: values[4],
    };
    openForm('edit', data);
  }
  if (action === 'delete') {
    if (confirm('¿Delete este project?')) {
      try {
        await deleteProject(id);
        showToast('Project eliminado', 'success');
        await loadProjects();
      } catch (e2) {
        console.error(e2);
        showToast(e2.message || 'Error eliminando project', 'error');
      }
    }
  }
});

// Form submit handler (create or update)
form?.addEventListener('submit', async (e) => {
  e.preventDefault();
  const idValue = inputId.value;
  const payload = {
    name: inputName.value?.trim() || null,
    description: inputDescription.value?.trim() || null,
    status: inputStatus.value?.trim() || null,
    responsible: inputResponsible.value?.trim() || null,
  };

  try {
    if (idValue) {
      await updateProject(idValue, payload);
      showToast('Project actualizado', 'success');
    } else {
      await createProject(payload);
      showToast('Project creado', 'success');
    }
    closeForm();
    await loadProjects();
  } catch (e2) {
    console.error(e2);
    showToast(e2.message || 'Error guardando project', 'error');
  }
});

// Initial load
loadProjects();
