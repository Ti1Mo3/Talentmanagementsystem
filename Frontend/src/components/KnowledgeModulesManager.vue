<template>
  <div class="knowledge-areas-wrapper">
    <h2>Wissensbausteine verwalten</h2>
    <!-- Filter Dropdown -->
    <div style="width:100%;max-width:500px;margin-bottom:1.2rem;display:flex;align-items:center;gap:1.2rem;">
      <select id="areaFilter" v-model="selectedAreaId" @change="onAreaFilterChange" class="add-input" style="min-width:180px;">
        <option :value="null">Wissensbereich auswählen</option>
        <option v-for="bereich in bereiche" :key="bereich.id" :value="bereich.id">{{ bereich.name }}</option>
      </select>
    </div>
    <div v-if="store.loading" class="loading-indicator">
      <svg class="spinner" width="32" height="32" viewBox="0 0 50 50"><circle class="path" cx="25" cy="25" r="20" fill="none" stroke-width="5"></circle></svg>
      <span>Wissensbausteine werden geladen ...</span>
    </div>
    <div v-else class="knowledge-areas-table">
      <div class="table-header">
        <span>Wissensbereich</span>
        <span>Name</span>
        <span>Level</span>
        <span>Reihenfolge</span>
        <span>Einarbeitung</span>
        <span class="actions-header"></span>
      </div>
      <div class="table-row" v-for="modul in module" :key="modul.id">
        <!-- Wissensbereich -->
        <span>
          {{ bereiche.find(b => b.id === modul.wissensbereichId)?.name }}
        </span>
        <!-- Name -->
        <span v-if="editingId !== modul.id">{{ modul.name }}</span>
        <span v-else>
          <input v-model="editedModule.name" type="text" class="add-input" placeholder="Name des Wissensbausteins" />
        </span>
        <!-- Level -->
        <span v-if="editingId !== modul.id">{{ modul.level }}</span>
        <span v-else>
          <select v-model="editedModule.level" class="add-input">
            <option disabled value="">Level auswählen</option>
            <option value="GRUNDWISSEN">GRUNDWISSEN</option>
            <option value="BERATERWISSEN">BERATERWISSEN</option>
            <option value="EXPERTENWISSEN">EXPERTENWISSEN</option>
          </select>
        </span>
        <!-- Reihenfolge -->
        <span v-if="editingId !== modul.id">{{ modul.reihenfolge }}</span>
        <span v-else>
          <input v-model.number="editedModule.reihenfolge" type="number" min="1" max="10" class="add-input" placeholder="Reihenfolge" />
        </span>
        <!-- Einarbeitung -->
        <span v-if="editingId !== modul.id">
          <input type="checkbox" disabled :checked="modul.einarbeitung" />
        </span>
        <span v-else>
          <input type="checkbox" v-model="editedModule.einarbeitung" />
        </span>
        <!-- Aktionen -->
        <span v-if="editingId !== modul.id" class="actions">
          <button class="icon-btn" @click="editModule(modul)" title="Bearbeiten">
            <svg width="24" height="24" viewBox="0 0 20 20" fill="none"><path d="M4 13.5V16h2.5l7.06-7.06-2.5-2.5L4 13.5z" stroke="#2563eb" stroke-width="1.5"/><path d="M13.06 6.44l1.5-1.5a1 1 0 0 1 1.41 0l0.59 0.59a1 1 0 0 1 0 1.41l-1.5 1.5-2.5-2.5z" stroke="#2563eb" stroke-width="1.5"/></svg>
          </button>
          <button class="icon-btn" @click="deleteModule(modul)" title="Löschen">
            <svg width="24" height="24" viewBox="0 0 20 20" fill="none"><path d="M6 7v7a2 2 0 0 0 2 2h4a2 2 0 0 0 2-2V7" stroke="#ef4444" stroke-width="1.5"/><path d="M9 10v4m2-4v4M3 7h14M8 7V5a2 2 0 0 1 2-2h0a2 2 0 0 1 2 2v2" stroke="#334155" stroke-width="1.5"/></svg>
          </button>
        </span>
        <span v-else class="actions" style="gap: 0.7rem; margin-left: 1.5rem;">
          <button type="button" class="save-btn" @click="saveEditModule(modul)" :disabled="!editedModule.name?.trim() || !editedModule.level || !editedModule.wissensbereichId || !editedModule.reihenfolge">Speichern</button>
          <button type="button" class="cancel-btn" @click="cancelEdit">Abbrechen</button>
        </span>
      </div>
      <div class="add-row">
        <button class="add-btn" @click="addModule" title="Neuen Wissensbaustein hinzufügen" v-if="!adding">
          <svg width="28" height="28" viewBox="0 0 20 20" fill="none"><circle cx="10" cy="10" r="9" fill="#22c55e"/><path d="M10 6v8M6 10h8" stroke="#fff" stroke-width="2" stroke-linecap="round"/></svg>
        </button>
        <form v-else @submit.prevent="saveNewModule" class="add-form add-form-grid">
          <label class="add-label">Wissensbereich</label>
          <select v-model="newModule.wissensbereichId" class="add-input">
            <option disabled value="">Wissensbereich auswählen</option>
            <option v-for="bereich in bereiche" :key="bereich.id" :value="bereich.id">{{ bereich.name }}</option>
          </select>
          <input v-model="newModule.name" type="text" placeholder="Name des Wissensbausteins" autofocus class="add-input" />
          <select v-model="newModule.level" class="add-input">
            <option disabled value="">Level auswählen</option>
            <option value="GRUNDWISSEN">GRUNDWISSEN</option>
            <option value="BERATERWISSEN">BERATERWISSEN</option>
            <option value="EXPERTENWISSEN">EXPERTENWISSEN</option>
          </select>
          <input v-model.number="newModule.reihenfolge" type="number" min="1" max="10" class="add-input" placeholder="Reihenfolge" />
          <label class="checkbox-label">
            <input type="checkbox" v-model="newModule.einarbeitung" />
            Einarbeitung
          </label>
          <div style="display: flex; gap: 0.7rem;">
            <button type="submit" class="save-btn" :disabled="!newModule.name?.trim() || !newModule.level || !newModule.wissensbereichId || !newModule.reihenfolge">Speichern</button>
            <button type="button" class="cancel-btn" @click="cancelAdd">Abbrechen</button>
          </div>
        </form>
      </div>
    </div>
    <div v-if="addError" class="modal-overlay">
      <div class="modal error-modal">
        <h3>Fehler</h3>
        <p>{{ addError }}</p>
        <div class="modal-actions">
          <button class="save-btn" @click="addError = null">OK</button>
        </div>
      </div>
    </div>
    <div v-if="editError" class="modal-overlay">
      <div class="modal error-modal">
        <h3>Fehler</h3>
        <p>{{ editError }}</p>
        <div class="modal-actions">
          <button class="save-btn" @click="editError = null">OK</button>
        </div>
      </div>
    </div>
    <div v-if="showDeleteModal" class="modal-overlay">
      <div class="modal">
        <h3>Wissensbaustein löschen</h3>
        <p>Möchten Sie den Wissensbaustein "{{ moduleToDelete?.name }}" wirklich löschen?</p>
        <div class="modal-actions">
          <button class="save-btn" @click="confirmDeleteModule">Löschen</button>
          <button class="cancel-btn" @click="cancelDeleteModule">Abbrechen</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import apiService from '../services/apiService';
import { useKnowledgeStore } from '../store/knowledge';

// Pinia Store
const store = useKnowledgeStore();

// State
const module = ref<any[]>([]); // Wissensbausteine
const bereiche = ref<any[]>([]); // Wissensbereiche
const newModule = ref({ name: '', level: '', einarbeitung: false, reihenfolge: 1, wissensbereichId: null as number | null });
const adding = ref(false);
const showDeleteModal = ref(false);
const moduleToDelete = ref<any | null>(null);
const editingId = ref<number | null>(null);
const editedModule = ref({ name: '', level: '', einarbeitung: false, reihenfolge: 1, wissensbereichId: null as number | null });
const addError = ref<string | null>(null);
const editError = ref<string | null>(null);
const selectedAreaId = ref<number|null>(null);

// Daten laden
async function fetchModules() {
  store.setLoading(true);
  store.setError(null);
  try {
    const [modulRes, bereichRes] = await Promise.all([
      apiService.get('/wissensbaustein'),
      apiService.get('/wissensbereich'),
    ]);
    module.value = modulRes.data;
    bereiche.value = bereichRes.data;
    store.setItems(modulRes.data);
  } catch (error: any) {
    store.setError('Fehler beim Laden der Wissensbausteine');
  } finally {
    store.setLoading(false);
  }
}

async function fetchModulesByBereich(bereichId: number) {
  store.setLoading(true);
  store.setError(null);
  try {
    const modulRes = await apiService.get(`/wissensbaustein/byWissensbereich/${bereichId}`);
    module.value = modulRes.data;
    store.setItems(modulRes.data);
  } catch (error: any) {
    store.setError('Fehler beim Filtern der Wissensbausteine');
  } finally {
    store.setLoading(false);
  }
}

onMounted(fetchModules);

// Editieren
function editModule(modul: any) {
  editingId.value = modul.id;
  editedModule.value = {
    name: modul.name,
    level: modul.level,
    einarbeitung: modul.einarbeitung,
    reihenfolge: modul.reihenfolge,
    wissensbereichId: modul.wissensbereichId ?? null,
  };
}

function cancelEdit() {
  editingId.value = null;
  editedModule.value = { name: '', level: '', einarbeitung: false, reihenfolge: 1, wissensbereichId: null };
}

async function saveEditModule(modul: any) {
  if (!editedModule.value.name?.trim() || !editedModule.value.level || !editedModule.value.wissensbereichId || !editedModule.value.reihenfolge) return;
  store.setLoading(true);
  store.setError(null);
  editError.value = null;
  try {
    const response = await apiService.put(`/wissensbaustein/${modul.id}`, {
      name: editedModule.value.name,
      level: editedModule.value.level,
      einarbeitung: editedModule.value.einarbeitung,
      reihenfolge: editedModule.value.reihenfolge,
      wissensbereichId: editedModule.value.wissensbereichId,
    });
    const idx = module.value.findIndex((m) => m.id === modul.id);
    if (idx !== -1) {
      module.value[idx] = response.data;
      store.setItems(module.value);
    }
    editingId.value = null;
    editedModule.value = { name: '', level: '', einarbeitung: false, reihenfolge: 1, wissensbereichId: null };
    selectedAreaId.value = null;
    await fetchModules();
  } catch (error: any) {
    editError.value = error?.response?.data || 'Fehler beim Bearbeiten des Wissensbausteins';
  } finally {
    store.setLoading(false);
  }
}

// Löschen
function deleteModule(modul: any) {
  moduleToDelete.value = modul;
  showDeleteModal.value = true;
}

async function confirmDeleteModule() {
  if (!moduleToDelete.value) return;
  store.setLoading(true);
  store.setError(null);
  try {
    await apiService.delete(`/wissensbaustein/${moduleToDelete.value.id}`);
    module.value = module.value.filter(m => m.id !== moduleToDelete.value!.id);
    store.setItems(module.value);
    showDeleteModal.value = false;
    moduleToDelete.value = null;
  } catch (error: any) {
    store.setError('Fehler beim Löschen des Wissensbausteins');
  } finally {
    store.setLoading(false);
  }
}

function cancelDeleteModule() {
  showDeleteModal.value = false;
  moduleToDelete.value = null;
}

// Hinzufügen
function addModule() {
  adding.value = true;
  newModule.value = { name: '', level: '', einarbeitung: false, reihenfolge: 1, wissensbereichId: null };
}

async function saveNewModule() {
  if (!newModule.value.name?.trim() || !newModule.value.level || !newModule.value.wissensbereichId || !newModule.value.reihenfolge) return;
  store.setLoading(true);
  store.setError(null);
  addError.value = null;
  try {
    const response = await apiService.post('/wissensbaustein', {
      name: newModule.value.name,
      level: newModule.value.level,
      einarbeitung: newModule.value.einarbeitung,
      reihenfolge: newModule.value.reihenfolge,
      wissensbereichId: newModule.value.wissensbereichId,
    });
    module.value.unshift(response.data);
    store.setItems(module.value);
    adding.value = false;
    newModule.value = { name: '', level: '', einarbeitung: false, reihenfolge: 1, wissensbereichId: null };
    selectedAreaId.value = null;
    await fetchModules();
  } catch (error: any) {
    addError.value = error?.response?.data || 'Fehler beim Hinzufügen des Wissensbausteins';
  } finally {
    store.setLoading(false);
  }
}

function cancelAdd() {
  adding.value = false;
  newModule.value = { name: '', level: '', einarbeitung: false, reihenfolge: 1, wissensbereichId: null };
  addError.value = null;
}

function onAreaFilterChange() {
  if (selectedAreaId.value) {
    fetchModulesByBereich(selectedAreaId.value);
  } else {
    fetchModules();
  }
}

function resetAreaFilter() {
  selectedAreaId.value = null;
  fetchModules();
}
</script>

<style scoped>
.knowledge-areas-wrapper {
  width: 100vw;
  max-width: 100vw;
  padding-left: 2vw;
  padding-right: 2vw;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
}
h2 {
  font-size: clamp(1.5rem, 2.8vw, 2.3rem);
  font-weight: 800;
  color: #1e293b;
  margin-bottom: 2vw;
  text-align: center;
  letter-spacing: 0.01em;
}
.knowledge-areas-table {
  width: 100%;
  max-width: 100%;
  min-width: 0;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 16px rgba(37,99,235,0.09);
  padding: 2.5rem 2rem 4rem 2rem;
  display: flex;
  flex-direction: column;
  gap: 0.7rem;
}

.table-header, .table-row {
  display: grid;
  grid-template-columns: 2fr 2fr 1.2fr 1fr 1.2fr 1fr;
  align-items: center;
  font-size: 1.12rem;
  padding: 0.4rem 0;
}
.table-header > span,
.table-row > span,
.table-row > form {
  display: flex;
  align-items: center;
}
.table-header > span,
.table-row > span {
  justify-content: flex-start;
}
.table-header > span:nth-child(3),
.table-row > span:nth-child(3),
.table-row > form:nth-child(3),
.table-header > span:nth-child(4),
.table-row > span:nth-child(4),
.table-row > form:nth-child(4),
.table-header > span:nth-child(5),
.table-row > span:nth-child(5),
.table-row > form:nth-child(5) {
  justify-content: center;
}
.table-header > span:last-child,
.table-row > span:last-child,
.table-row > form:last-child {
  justify-content: flex-end;
}
.table-header {
  font-weight: 700;
  color: #334155;
  border-bottom: 1.5px solid #e0e0e0;
  margin-bottom: 0.7rem;
}
.table-row {
  background: #f8fafc;
  border-radius: 10px;
  margin-bottom: 0.2rem;
  box-shadow: 0 1px 6px rgba(37,99,235,0.06);
  padding: 0.6rem 1rem;
  transition: box-shadow 0.2s;
}
.table-row:hover {
  box-shadow: 0 2px 10px rgba(37,99,235,0.13);
}
.table-row input[type="checkbox"] {
  accent-color: #22c55e;
  width: 1.1em;
  height: 1.1em;
  margin-right: 0.5em;
  cursor: pointer;
}
.table-row input[type="checkbox"] {
  accent-color: #22c55e;
  width: 1.1em;
  height: 1.1em;
  margin-right: 0.5em;
  cursor: pointer;
}
.add-row {
  display: flex;
  justify-content: flex-start;
  margin-top: 0.9rem;
  gap: 0.7rem;
}
.add-form {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 0.8fr 0.8fr;
  grid-template-rows: auto;
  gap: 0.7rem;
  align-items: center;
  margin-top: 0.2rem;
  overflow: visible;
}
.add-form.add-form-grid {
  display: grid;
  grid-template-columns: auto 1.5fr 1.5fr 1.2fr 1.2fr;
  align-items: center;
  gap: 0.7rem;
  margin-top: 0.2rem;
  overflow: visible;
}
.add-input {
  flex: unset;
  width: 100%;
  padding: 0.45rem 0.8rem;
  border: 1.5px solid #cbd5e1;
  border-radius: 7px;
  font-size: 1.05rem;
  position: relative;
  z-index: 1;
}
.add-label {
  font-weight: 600;
  color: #334155;
  margin-right: 0;
  margin-left: 0;
  white-space: nowrap;
}
.add-form label {
  display: flex;
  align-items: center;
  gap: 0.7em;
  font-size: 1.13rem;
  color: #334155;
  font-weight: 700;
  margin-left: 1.2em;
  white-space: nowrap;
}
.add-form select.add-input {
  appearance: none;
  background: #fff url('data:image/svg+xml;utf8,<svg fill="%23334155" height="20" viewBox="0 0 20 20" width="20" xmlns="http://www.w3.org/2000/svg"><path d="M7.293 8.293a1 1 0 011.414 0L10 9.586l1.293-1.293a1 1 0 111.414 1.414l-2 2a1 1 0 01-1.414 0l-2-2a1 1 0 010-1.414z"/></svg>') no-repeat right 0.8em center/1.2em 1.2em;
  padding-right: 2.2em;
  cursor: pointer;
  /* Dropdown nach unten bevorzugen */
  /* Browser öffnen Dropdowns nach unten, wenn genug Platz ist. Durch overflow: visible und z-index wird das Verhalten unterstützt. */
}
.save-btn {
  background: #22c55e;
  color: #fff;
  border: none;
  border-radius: 7px;
  padding: 0.45rem 1.1rem;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.2s;
  font-size: 1rem;
}
.save-btn:disabled {
  background: #a7f3d0;
  cursor: not-allowed;
}
.cancel-btn {
  background: #e0e7ef;
  color: #334155;
  border: none;
  border-radius: 7px;
  padding: 0.45rem 1rem;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.2s;
  font-size: 1rem;
  margin-left: 0;
}
.cancel-btn:hover {
  background: #cbd5e1;
}
.modal-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(30, 41, 59, 0.25);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 24px rgba(37,99,235,0.13);
  padding: 2rem 1.5rem 1.5rem 1.5rem;
  min-width: 300px;
  max-width: 90vw;
  text-align: center;
}
.modal h3 {
  margin-bottom: 0.7rem;
  color: #ef4444;
  font-size: 1.2rem;
  font-weight: 800;
}
.modal-actions {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 1.2rem;
}
.error-modal {
  border: 2px solid #ef4444;
  box-shadow: 0 4px 24px rgba(239,68,68,0.13);
}
.error-modal h3 {
  color: #ef4444;
}
.error-modal p {
  color: #b91c1c;
  font-weight: 600;
  margin: 1em 0 0.5em 0;
}
.add-error {
  color: #ef4444;
  background: #fef2f2;
  border: 1px solid #fecaca;
  border-radius: 6px;
  padding: 0.6em 1em;
  margin-top: 0.7em;
  font-size: 1.02rem;
  font-weight: 600;
  max-width: 500px;
  box-shadow: 0 1px 4px rgba(239,68,68,0.07);
}
.loading-indicator {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 180px;
  gap: 1.2rem;
  color: #2563eb;
  font-size: 1.15rem;
  font-weight: 600;
}
.spinner {
  animation: spin 1s linear infinite;
}
@keyframes spin {
  100% { transform: rotate(360deg); }
}
.path {
  stroke: #2563eb;
  stroke-linecap: round;
}
@media (max-width: 600px) {
  .knowledge-areas-table {
    max-width: 99vw;
    padding: 1rem 0.2rem 1rem 0.2rem;
  }
  h2 {
    font-size: 1.1rem;
  }
  .table-header, .table-row {
    font-size: 0.98rem;
    padding: 0.2rem 0;
  }
}
</style>
