<template>
  <div class="datenmanager">
    <TmsHeader :selectedMenu="selectedMenu" @menu-select="onMenuSelect" />
    <div class="content-area">
      <component :is="currentComponent" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import TmsHeader from '../components/TmsHeader.vue';
import KnowledgeAreasManager from '../components/KnowledgeAreasManager.vue';
import KnowledgeDomainsManager from '../components/KnowledgeDomainsManager.vue';
import KnowledgeModulesManager from '../components/KnowledgeModulesManager.vue';
import LearningPathsManager from '../components/LearningPathsManager.vue';

const menuItems = [
  { key: 'areas', label: 'Wissensgebiete verwalten', component: KnowledgeAreasManager },
  { key: 'domains', label: 'Wissensbereiche verwalten', component: KnowledgeDomainsManager },
  { key: 'modules', label: 'Wissensbausteine verwalten', component: KnowledgeModulesManager },
  { key: 'paths', label: 'Ausbildungspfade verwalten', component: LearningPathsManager },
];

const selectedMenu = ref('areas');
const currentComponent = computed(() => {
  const found = menuItems.find((item) => item.key === selectedMenu.value);
  return found ? found.component : null;
});

function onMenuSelect(key: string) {
  selectedMenu.value = key;
}
</script>

<style scoped>
.datenmanager {
  height: 100%;   
  width: 100%;      
  display: flex;
  flex-direction: column;
  align-items: stretch;
  background: #f8fafc;
  margin: 0;
}

.menu-bar {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 1rem;
  padding: 1rem;
  background: transparent;
}

.content-area {
  flex-grow: 1;
  width: 100%;
  background: #fff;
  border-radius: 0;
  box-shadow: none;
  display: flex;
  align-items: flex-start;
  justify-content: center;
}

@media (max-width: 1200px) {
  .content-area {
    padding: 1.5rem;
  }
}

@media (max-width: 900px) {
  .content-area {
    padding: 1rem;
  }
  h1 {
    font-size: 2rem;
  }
}

@media (max-width: 600px) {
  .menu-bar {
    flex-direction: column;
    align-items: center;
  }
  h1 {
    margin-top: 1rem;
    font-size: 1.5rem;
  }
}
</style>
