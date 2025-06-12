<template>
  <div class="datenmanager">
    <h1>Datenmanager</h1>
    <button @click="fetchItems">Wissensbausteine laden</button>
    <ul>
      <li v-for="item in items" :key="item.id">
        {{ item.name }}
        <!-- Bearbeiten/Löschen Buttons -->
      </li>
    </ul>
    <div v-if="loading">Lade Daten...</div>
    <div v-if="error" class="error">{{ error }}</div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import { useKnowledgeStore } from '../store/knowledge';
import apiService from '../services/apiService';

const store = useKnowledgeStore();
const { items, loading, error } = store;

const fetchItems = async () => {
  store.setLoading(true);
  store.setError(null);
  try {
    const response = await apiService.get('/knowledge');
    store.setItems(response.data);
  } catch (e: any) {
    store.setError(e.message || 'Fehler beim Laden');
  } finally {
    store.setLoading(false);
  }
};

onMounted(fetchItems);
</script>

<style scoped>
.datenmanager { max-width: 600px; margin: 2rem auto; }
.error { color: red; }
</style>
