<template>
  <div class="knowledge-areas-wrapper">
    <h2>Wissensbereiche verwalten</h2>
    <div v-if="store.loading" class="loading-indicator">
      <svg class="spinner" width="32" height="32" viewBox="0 0 50 50"><circle class="path" cx="25" cy="25" r="20" fill="none" stroke-width="5"></circle></svg>
      <span>Wissensbereiche werden geladen ...</span>
    </div>
    <div v-else class="knowledge-areas-table">
      <div class="table-header">
        <span>Wissensgebiet</span>
        <span>Wissensbereich</span>
        <span>Einarbeitung</span>
        <span class="actions-header"></span>
      </div>
      <div class="table-row" v-for="section in sections" :key="section.id">
        <span>{{ section.wissensgebiet?.name }}</span>
        <span v-if="editingId !== section.id">{{ section.name }}</span>
        <form v-else @submit.prevent="saveEditSection(section)" class="add-form" style="flex:1;">
          <input v-model="editedSection.name" type="text" class="add-input" autofocus />
        </form>
        <span v-if="editingId !== section.id">
          <input type="checkbox" disabled :checked="section.einarbeitung" />
        </span>
        <form v-else @submit.prevent="saveEditSection(section)" class="add-form" style="flex:1;">
          <label style="display:flex;align-items:center;gap:0.5em;">
            <input type="checkbox" v-model="editedSection.einarbeitung" />
            Einarbeitung erforderlich
          </label>
          <select v-model="editedSection.wissensgebietId" class="add-input">
            <option v-for="area in areas" :key="area.id" :value="area.id">{{ area.name }}</option>
          </select>
          <button type="submit" class="save-btn" :disabled="!editedSection.name?.trim()">Speichern</button>
          <button type="button" class="cancel-btn" @click="cancelEdit">Abbrechen</button>
        </form>
        <span class="actions" v-if="editingId !== section.id">
          <button class="icon-btn" @click="editSection(section)" title="Bearbeiten">
            <svg width="24" height="24" viewBox="0 0 20 20" fill="none"><path d="M4 13.5V16h2.5l7.06-7.06-2.5-2.5L4 13.5z" stroke="#2563eb" stroke-width="1.5"/><path d="M13.06 6.44l1.5-1.5a1 1 0 0 1 1.41 0l0.59 0.59a1 1 0 0 1 0 1.41l-1.5 1.5-2.5-2.5z" stroke="#2563eb" stroke-width="1.5"/></svg>
          </button>
          <button class="icon-btn" @click="deleteSection(section)" title="Löschen">
            <svg width="24" height="24" viewBox="0 0 20 20" fill="none"><path d="M6 7v7a2 2 0 0 0 2 2h4a2 2 0 0 0 2-2V7" stroke="#ef4444" stroke-width="1.5"/><path d="M9 10v4m2-4v4M3 7h14M8 7V5a2 2 0 0 1 2-2h0a2 2 0 0 1 2 2v2" stroke="#334155" stroke-width="1.5"/></svg>
          </button>
        </span>
      </div>
      <div class="add-row">
        <button class="add-btn" @click="addSection" title="Neuen Wissensbereich hinzufügen" v-if="!adding">
          <svg width="28" height="28" viewBox="0 0 20 20" fill="none"><circle cx="10" cy="10" r="9" fill="#22c55e"/><path d="M10 6v8M6 10h8" stroke="#fff" stroke-width="2" stroke-linecap="round"/></svg>
        </button>
        <form v-else @submit.prevent="saveNewSection" class="add-form">
          <input v-model="newSection.name" type="text" placeholder="Name des Wissensbereichs" autofocus class="add-input" />
          <label style="display:flex;align-items:center;gap:0.5em;">
            <input type="checkbox" v-model="newSection.einarbeitung" />
            Einarbeitung erforderlich
          </label>
          <select v-model="newSection.wissensgebietId" class="add-input">
            <option v-for="area in areas" :key="area.id" :value="area.id">{{ area.name }}</option>
          </select>
          <button type="submit" class="save-btn" :disabled="!newSection.name?.trim()">Speichern</button>
          <button type="button" class="cancel-btn" @click="cancelAdd">Abbrechen</button>
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
        <h3>Wissensbereich löschen</h3>
        <p>Möchten Sie den Wissensbereich "{{ sectionToDelete?.name }}" wirklich löschen?</p>
        <div class="modal-actions">
          <button class="save-btn" @click="confirmDeleteSection">Löschen</button>
          <button class="cancel-btn" @click="cancelDeleteSection">Abbrechen</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import apiService from '../services/apiService';
import { useKnowledgeStore } from '../store/knowledge';
import type { KnowledgeArea } from '../models/KnowledgeArea';
import type { KnowledgeSection } from '../models/KnowledgeSection';

const store = useKnowledgeStore();
const sections = ref<KnowledgeSection[]>([]);
const areas = ref<KnowledgeArea[]>([]);
const newSection = ref<{ name: string; einarbeitung: boolean; wissensgebietId: number | null }>({ name: '', einarbeitung: false, wissensgebietId: null });
const adding = ref(false);
const showDeleteModal = ref(false);
const sectionToDelete = ref<KnowledgeSection | null>(null);
const editingId = ref<number | null>(null);
const editedSection = ref<{ name: string; einarbeitung: boolean; wissensgebietId: number | null }>({ name: '', einarbeitung: false, wissensgebietId: null });
const addError = ref<string | null>(null);
const editError = ref<string | null>(null);

async function fetchSections() {
  store.setLoading(true);
  store.setError(null);
  try {
    const [sectionRes, areaRes] = await Promise.all([
      apiService.get('/wissensbereich'),
      apiService.get('/wissensgebiet'),
    ]);
    sections.value = sectionRes.data;
    areas.value = areaRes.data;
    store.setItems(sectionRes.data);
  } catch (error: any) {
    store.setError('Fehler beim Laden der Wissensbereiche');
  } finally {
    store.setLoading(false);
  }
}

onMounted(fetchSections);

function editSection(section: KnowledgeSection) {
  editingId.value = section.id;
  editedSection.value = {
    name: section.name,
    einarbeitung: section.einarbeitung,
    wissensgebietId: section.wissensgebiet?.id ?? null,
  };
}

function cancelEdit() {
  editingId.value = null;
  editedSection.value = { name: '', einarbeitung: false, wissensgebietId: null };
}

async function saveEditSection(section: KnowledgeSection) {
  if (!editedSection.value.name?.trim() || !editedSection.value.wissensgebietId) return;
  store.setLoading(true);
  store.setError(null);
  editError.value = null;
  try {
    const response = await apiService.put(`/wissensbereich/${section.id}`, {
      name: editedSection.value.name,
      einarbeitung: editedSection.value.einarbeitung,
      wissensgebietId: editedSection.value.wissensgebietId,
    });
    const idx = sections.value.findIndex((s) => s.id === section.id);
    if (idx !== -1) {
      sections.value[idx] = response.data;
      store.setItems(sections.value);
    }
    editingId.value = null;
    editedSection.value = { name: '', einarbeitung: false, wissensgebietId: null };
  } catch (error: any) {
    if (error?.response?.data) {
      editError.value = error.response.data;
    } else {
      editError.value = 'Fehler beim Bearbeiten des Wissensbereichs';
    }
  } finally {
    store.setLoading(false);
  }
}

function deleteSection(section: KnowledgeSection) {
  sectionToDelete.value = section;
  showDeleteModal.value = true;
}
async function confirmDeleteSection() {
  if (!sectionToDelete.value) return;
  store.setLoading(true);
  store.setError(null);
  try {
    await apiService.delete(`/wissensbereich/${sectionToDelete.value.id}`);
    sections.value = sections.value.filter(s => s.id !== sectionToDelete.value!.id);
    store.setItems(sections.value);
    showDeleteModal.value = false;
    sectionToDelete.value = null;
  } catch (error: any) {
    store.setError('Fehler beim Löschen des Wissensbereichs');
  } finally {
    store.setLoading(false);
  }
}
function cancelDeleteSection() {
  showDeleteModal.value = false;
  sectionToDelete.value = null;
}
function addSection() {
  adding.value = true;
  newSection.value = { name: '', einarbeitung: false, wissensgebietId: null };
}

async function saveNewSection() {
  if (!newSection.value.name?.trim() || !newSection.value.wissensgebietId) return;
  store.setLoading(true);
  store.setError(null);
  addError.value = null;
  try {
    const response = await apiService.post('/wissensbereich', {
      name: newSection.value.name,
      einarbeitung: newSection.value.einarbeitung,
      wissensgebietId: newSection.value.wissensgebietId,
    });
    sections.value.unshift(response.data);
    store.setItems(sections.value);
    adding.value = false;
    newSection.value = { name: '', einarbeitung: false, wissensgebietId: null };
  } catch (error: any) {
    if (error?.response?.data) {
      addError.value = error.response.data;
    } else {
      addError.value = 'Fehler beim Hinzufügen des Wissensbereichs';
    }
  } finally {
    store.setLoading(false);
  }
}

function cancelAdd() {
  adding.value = false;
  newSection.value = { name: '', einarbeitung: false, wissensgebietId: null };
  addError.value = null;
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
  grid-template-columns: 2fr 2fr 1fr 1fr;
  align-items: center;
  font-size: 1.12rem;
  padding: 0.4rem 0;
}
.table-header > span:nth-child(3),
.table-row > span:nth-child(3),
.table-row > form:nth-child(3) {
  justify-content: center;
  display: flex;
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
.table-row > span, .table-row > form {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}
.table-row > span:nth-child(2) {
  justify-content: flex-start;
}
.table-row > span:nth-child(3),
.table-row > form:nth-child(3) {
  justify-content: center;
}
.table-row > span:nth-child(4),
.table-row > form:nth-child(4) {
  justify-content: flex-end;
}
.table-row > span:first-child {
  flex: 2;
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
  grid-template-columns: 2fr 1fr 1fr 1fr;
  gap: 0.7rem;
  align-items: center;
  margin-top: 0.2rem;
}
.add-input {
  flex: unset;
  width: 100%;
  padding: 0.45rem 0.8rem;
  border: 1.5px solid #cbd5e1;
  border-radius: 7px;
  font-size: 1.05rem;
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
