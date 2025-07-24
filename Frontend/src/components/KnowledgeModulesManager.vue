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
        <span>Wissensgebiet</span>
        <span>Wissensbereich</span>
        <span>Name</span>
        <span>Level</span>
        <span>Einarbeitung</span>
        <span class="actions-header"></span>
      </div>
      <div class="table-row" v-for="modul in module" :key="modul.id" style="column-gap: 0;">
        <template v-if="editingId !== modul.id">
          <span>
            {{ gebiete.find(g => g.id === modul.wissensgebietId)?.name }}
          </span>
          <span>
            {{ bereiche.find(b => b.id === modul.wissensbereichId)?.name }}
          </span>
          <span>{{ modul.name }}</span>
          <span>{{ modul.level }}</span>
          <span>
            <input type="checkbox" disabled :checked="modul.einarbeitung" />
          </span>
          <span class="actions">
            <button class="icon-btn" @click="editModule(modul)" title="Bearbeiten">
              <svg width="24" height="24" viewBox="0 0 20 20" fill="none"><path d="M4 13.5V16h2.5l7.06-7.06-2.5-2.5L4 13.5z" stroke="#2563eb" stroke-width="1.5"/><path d="M13.06 6.44l1.5-1.5a1 1 0 0 1 1.41 0l0.59 0.59a1 1 0 0 1 0 1.41l-1.5 1.5-2.5-2.5z" stroke="#2563eb" stroke-width="1.5"/></svg>
            </button>
            <button class="icon-btn" @click="deleteModule(modul)" title="Löschen">
              <svg width="24" height="24" viewBox="0 0 20 20" fill="none"><path d="M6 7v7a2 2 0 0 0 2 2h4a2 2 0 0 0 2-2V7" stroke="#ef4444" stroke-width="1.5"/><path d="M9 10v4m2-4v4M3 7h14M8 7V5a2 2 0 0 1 2-2h0a2 2 0 0 1 2 2v2" stroke="#334155" stroke-width="1.5"/></svg>
            </button>
          </span>
        </template>
        <template v-else>
          <form @submit.prevent="saveEditModule(modul)" class="add-form-2row" style="width:100%;margin-bottom:0;grid-column: 1 / -1;display: grid;grid-template-columns: 1fr;">
            <div class="add-form-row-1">
              <span class="add-form-field">
                <label class="add-form-label">Wissensgebiet</label>
                <select v-model="editedModule.wissensgebietId" class="add-input">
                  <option :value="null">Wissensgebiet auswählen</option>
                  <option v-for="gebiet in gebiete" :key="gebiet.id" :value="gebiet.id">{{ gebiet.name }}</option>
                </select>
              </span>
              <span class="add-form-field">
                <label class="add-form-label">Wissensbereich</label>
                <select v-model="editedModule.wissensbereichId" class="add-input" :disabled="!editedModule.wissensgebietId">
                  <option :value="null">Wissensbereich auswählen</option>
                  <option v-for="bereich in bereiche.filter(b => b.wissensgebietId === editedModule.wissensgebietId)" :key="bereich.id" :value="bereich.id">{{ bereich.name }}</option>
                </select>
              </span>
              <span class="add-form-field">
                <label class="add-form-label">Name des Wissensbausteins</label>
                <input v-model="editedModule.name" type="text" class="add-input" placeholder="Name des Wissensbausteins" />
              </span>
              <span class="add-form-field">
                <label class="add-form-label">Level</label>
                <select v-model="editedModule.level" class="add-input">
                  <option disabled value="">Level auswählen</option>
                  <option value="GRUNDWISSEN">GRUNDWISSEN</option>
                  <option value="BERATERWISSEN">BERATERWISSEN</option>
                  <option value="EXPERTENWISSEN">EXPERTENWISSEN</option>
                </select>
              </span>
            </div>
            <div class="add-form-row-2-fixed">
              <div class="add-form-field" style="display: flex; align-items: center; gap: 0.7rem;">
                <input type="checkbox" v-model="editedModule.einarbeitung" style="margin-right: 0.5em;" />
                <label class="add-form-label" style="margin-bottom:0;">Einarbeitung</label>
              </div>
              <div v-if="editedModule.einarbeitung" class="add-form-field">
                <label class="add-form-label">Reihenfolge</label>
                <input v-model.number="editedModule.reihenfolge" type="number" min="1" max="10" class="add-input" placeholder="Reihenfolge" />
              </div>
              <div class="add-form-field add-form-actions">
                <button type="submit" class="save-btn" :disabled="!editedModule.name?.trim() || !editedModule.level || !editedModule.wissensbereichId || !editedModule.wissensgebietId">Speichern</button>
                <button type="button" class="cancel-btn" @click="cancelEdit">Abbrechen</button>
              </div>
            </div>
          </form>
        </template>
      </div>
      <div class="add-row">
        <button class="add-btn" @click="addModule" title="Neuen Wissensbaustein hinzufügen" v-if="!adding">
          <svg width="28" height="28" viewBox="0 0 20 20" fill="none"><circle cx="10" cy="10" r="9" fill="#22c55e"/><path d="M10 6v8M6 10h8" stroke="#fff" stroke-width="2" stroke-linecap="round"/></svg>
        </button>
        <form v-else @submit.prevent="saveNewModule" class="add-form-2row">
          <div class="add-form-row-1">
            <span class="add-form-field">
              <label for="modul-gebiet" class="add-form-label">Wissensgebiet</label>
              <select id="modul-gebiet" v-model="newModule.wissensgebietId" class="add-input">
                <option :value="null">Wissensgebiet auswählen</option>
                <option v-for="gebiet in gebiete" :key="gebiet.id" :value="gebiet.id">{{ gebiet.name }}</option>
              </select>
            </span>
            <span class="add-form-field">
              <label for="modul-bereich" class="add-form-label">Wissensbereich</label>
              <select id="modul-bereich" v-model="newModule.wissensbereichId" class="add-input" :disabled="!newModule.wissensgebietId">
                <option :value="null">Wissensbereich auswählen</option>
                <option v-for="bereich in bereiche.filter(b => b.wissensgebietId === newModule.wissensgebietId)" :key="bereich.id" :value="bereich.id">{{ bereich.name }}</option>
              </select>
            </span>
            <span class="add-form-field">
              <label for="modul-name"  class="add-form-label">Name des Wissensbausteins</label>
              <input id="modul-name" v-model="newModule.name" type="text" placeholder="Name des Wissensbausteins" autofocus class="add-input" />
            </span>
            <span class="add-form-field">
              <label for="modul-level" class="add-form-label">Level</label>
              <select id="modul-level" v-model="newModule.level" class="add-input">
                <option disabled value="">Level auswählen</option>
                <option value="GRUNDWISSEN">GRUNDWISSEN</option>
                <option value="BERATERWISSEN">BERATERWISSEN</option>
                <option value="EXPERTENWISSEN">EXPERTENWISSEN</option>
              </select>
            </span>
          </div>
          <div class="add-form-row-2-fixed">
            <div class="add-form-field" style="display: flex; align-items: center; gap: 0.7rem;">
              <input id="modul-einarbeitung" type="checkbox" v-model="newModule.einarbeitung" style="margin-right: 0.5em;" />
              <label for="modul-einarbeitung" class="add-form-label" style="margin-bottom:0;">Einarbeitung</label>
            </div>
            <div v-if="newModule.einarbeitung" class="add-form-field">
              <label for="modul-reihenfolge"  class="add-form-label">Reihenfolge</label>
              <input id="modul-reihenfolge" v-model.number="newModule.reihenfolge" type="number" min="1" max="10" class="add-input" placeholder="Reihenfolge" />
            </div>
            <div class="add-form-field add-form-actions">
              <button type="submit" class="save-btn"
                :disabled="!newModule.name?.trim() || !newModule.level || !newModule.wissensbereichId || !newModule.wissensgebietId">Speichern</button>
              <button type="button" class="cancel-btn" @click="cancelAdd">Abbrechen</button>
            </div>
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
const newModule = ref({
  name: '',
  level: '',
  einarbeitung: false,
  reihenfolge: 1,
  wissensbereichId: null as number | null,
  wissensgebietId: null as number | null,
});const adding = ref(false);
const showDeleteModal = ref(false);
const moduleToDelete = ref<any | null>(null);
const editingId = ref<number | null>(null);
const editedModule = ref({
  name: '',
  level: '',
  einarbeitung: false,
  reihenfolge: 1,
  wissensbereichId: null as number | null,
  wissensgebietId: null as number | null,
});
const addError = ref<string | null>(null);
const editError = ref<string | null>(null);
const selectedAreaId = ref<number|null>(null);
const gebiete = ref<any[]>([]);

// Daten laden
async function fetchModules() {
  store.setLoading(true);
  store.setError(null);
  try {
    const [modulRes, bereichRes, gebietRes] = await Promise.all([
      apiService.get('/wissensbaustein'),
      apiService.get('/wissensbereich'),
      apiService.get('/wissensgebiet'),
    ]);
    module.value = modulRes.data;
    bereiche.value = bereichRes.data;
    gebiete.value = gebietRes.data;
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
    wissensgebietId: modul.wissensgebietId ?? null,
  };
}

function cancelEdit() {
  editingId.value = null;
  editedModule.value = { name: '', level: '', einarbeitung: false, reihenfolge: 1, wissensbereichId: null, wissensgebietId: null };
}

async function saveEditModule(modul: any) {
  if (!editedModule.value.name?.trim() || !editedModule.value.level || !editedModule.value.wissensbereichId || !editedModule.value.wissensgebietId) return;
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
      wissensgebietId: editedModule.value.wissensgebietId,
    });
    const idx = module.value.findIndex((m) => m.id === modul.id);
    if (idx !== -1) {
      module.value[idx] = response.data;
      store.setItems(module.value);
    }
    editingId.value = null;
    editedModule.value = { name: '', level: '', einarbeitung: false, reihenfolge: 1, wissensbereichId: null, wissensgebietId: null };
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
  newModule.value = {
    name: '',
    level: '',
    einarbeitung: false,
    reihenfolge: 1,
    wissensbereichId: null,
    wissensgebietId: null,
  };}

async function saveNewModule() {
  if (
    !newModule.value.name?.trim() ||
    !newModule.value.level ||
    !newModule.value.wissensbereichId ||
    !newModule.value.wissensgebietId ||
    !newModule.value.reihenfolge
  ) return;
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
      wissensgebietId: newModule.value.wissensgebietId,
    });
    module.value.unshift(response.data);
    store.setItems(module.value);
    adding.value = false;
    newModule.value = { name: '', level: '', einarbeitung: false, reihenfolge: 1, wissensbereichId: null, wissensgebietId: null };
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
  newModule.value = { name: '', level: '', einarbeitung: false, reihenfolge: 1, wissensbereichId: null, wissensgebietId: null };
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
  column-gap: 1.2rem;
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
.add-form-2row {
  width: 100%;
  background: #f8fafc;
  border-radius: 10px;
  margin-bottom: 0.2rem;
  box-shadow: 0 1px 6px rgba(37,99,235,0.06);
  padding: 0.6rem 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}
.add-form-row-1 {
  display: grid;
  grid-template-columns: 1.2fr 1.2fr 1.2fr 1.2fr;
  align-items: end;
  font-size: 1.12rem;
  gap: 2.0rem;
}
.add-form-row-1 .add-form-field:nth-child(4) {
  margin-left: 1.5rem;
}
.add-form-row-1 .add-form-field {
  min-width: 0;
}
.add-form-row-2 {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;
  align-items: center;
  font-size: 1.12rem;
  gap: 1rem;
}
.add-form-label {
  font-weight: 600;
  color: #334155;
  margin-right: 0.5em;
  white-space: nowrap;
  display: flex;
  align-items: center;
}
.add-form-empty {
  display: none;
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
.add-form-row-2-fixed {
  display: grid;
  grid-template-columns: 2fr 1.2fr 1.2fr 1.2fr 1.4fr;
  align-items: end;
  font-size: 1.12rem;
  gap: 1.2rem;
  margin-top: 0.2rem;
}
.add-form-field {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}
.add-form-actions {
  flex-direction: row !important;
  align-items: center;
  gap: 0.7rem;
  justify-content: flex-end;
  margin-top: 1.1em;
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
