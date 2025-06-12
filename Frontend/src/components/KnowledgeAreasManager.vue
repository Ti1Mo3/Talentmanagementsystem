<template>
  <div class="knowledge-areas-wrapper">
    <h2>Wissensgebiete verwalten</h2>
    <div class="knowledge-areas-table">
      <div class="table-header">
        <span>Wissensgebiet</span>
        <span class="actions-header"></span>
      </div>
      <div class="table-row" v-for="area in areas" :key="area.id">
        <span>{{ area.name }}</span>
        <span class="actions">
          <button class="icon-btn" @click="editArea(area)" title="Bearbeiten">
            <svg width="24" height="24" viewBox="0 0 20 20" fill="none"><path d="M4 13.5V16h2.5l7.06-7.06-2.5-2.5L4 13.5z" stroke="#2563eb" stroke-width="1.5"/><path d="M13.06 6.44l1.5-1.5a1 1 0 0 1 1.41 0l0.59 0.59a1 1 0 0 1 0 1.41l-1.5 1.5-2.5-2.5z" stroke="#2563eb" stroke-width="1.5"/></svg>
          </button>
          <button class="icon-btn" @click="deleteArea(area)" title="Löschen">
            <svg width="24" height="24" viewBox="0 0 20 20" fill="none"><path d="M6 7v7a2 2 0 0 0 2 2h4a2 2 0 0 0 2-2V7" stroke="#ef4444" stroke-width="1.5"/><path d="M9 10v4m2-4v4M3 7h14M8 7V5a2 2 0 0 1 2-2h0a2 2 0 0 1 2 2v2" stroke="#334155" stroke-width="1.5"/></svg>
          </button>
        </span>
      </div>
      <div class="add-row">
        <button class="add-btn" @click="addArea" title="Neues Wissensgebiet hinzufügen">
          <svg width="28" height="28" viewBox="0 0 20 20" fill="none"><circle cx="10" cy="10" r="9" fill="#22c55e"/><path d="M10 6v8M6 10h8" stroke="#fff" stroke-width="2" stroke-linecap="round"/></svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import apiService from '../services/apiService';
import { useKnowledgeStore } from '../store/knowledge';

const store = useKnowledgeStore();
const areas = ref<any[]>([]);

async function fetchAreas() {
  store.setLoading(true);
  store.setError(null);
  try {
    const response = await apiService.get('/wissensgebiet');
    console.log('Wissensgebiete geladen:', response.data);
    areas.value = response.data;
    store.setItems(response.data);
  } catch (error: any) {
    store.setError('Fehler beim Laden der Wissensgebiete');
  } finally {
    store.setLoading(false);
  }
}

onMounted(fetchAreas);

function editArea(area: any) {
  // Edit-Logik
}
function deleteArea(area: any) {
  // Delete-Logik
}
function addArea() {
  // Add-Logik
}
</script>

<style scoped>
.knowledge-areas-wrapper {
  width: 100%;
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
  max-width: 420px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(37,99,235,0.07);
  padding: 1.5rem 1rem 1rem 1rem;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}
.table-header, .table-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 1.08rem;
  padding: 0.3rem 0;
}
.table-header {
  font-weight: 700;
  color: #334155;
  border-bottom: 1px solid #e0e0e0;
  margin-bottom: 0.5rem;
}
.table-row {
  background: #f8fafc;
  border-radius: 8px;
  margin-bottom: 0.2rem;
  box-shadow: 0 1px 4px rgba(37,99,235,0.04);
  padding: 0.5rem 0.8rem;
  transition: box-shadow 0.2s;
}
.table-row:hover {
  box-shadow: 0 2px 8px rgba(37,99,235,0.10);
}
.actions {
  display: flex;
  gap: 0.5rem;
}
.icon-btn {
  background: none;
  border: none;
  color: #2563eb;
  font-size: 1.2rem;
  cursor: pointer;
  padding: 0.2rem 0.3rem;
  border-radius: 4px;
  transition: background 0.2s, color 0.2s;
  display: flex;
  align-items: center;
}
.icon-btn:hover {
  background: #e0e7ef;
}
.add-row {
  display: flex;
  justify-content: flex-start;
  margin-top: 0.7rem;
}
.add-btn {
  background: none;
  border: none;
  border-radius: 50%;
  width: 2.2rem;
  height: 2.2rem;
  font-size: 1.3rem;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.2s;
  box-shadow: none;
  padding: 0;
}
.add-btn:hover {
  background: #22c55e22;
}
.icon-btn svg {
  width: 1.3em;
  height: 1.3em;
}
.add-btn svg {
  width: 1.6em;
  height: 1.6em;
}
@media (max-width: 600px) {
  .knowledge-areas-table {
    max-width: 99vw;
    padding: 1rem 0.2rem 1rem 0.2rem;
  }
  h2 {
    font-size: 1.1rem;
  }
}
</style>
