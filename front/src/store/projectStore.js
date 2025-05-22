// projectStore.js
import { ref, watch } from 'vue'

export const useProjectStore = () => {
  // 当前选中的项目
  const currentProject = ref(null)

  // 设置当前项目
  const setCurrentProject = (project) => {
    console.log("设置当前项目:", project);

    // 确保项目对象包含所有必要的字段，标准化字段名称
    if (project) {
      // 获取ID值（可能是字符串或数字）
      let idValue = project.id || project.projectId || '';

      // 尝试将ID转换为数字
      let numericId = Number(idValue);
      if (isNaN(numericId)) {
        console.warn("项目ID不是有效的数字，使用原始值:", idValue);
        numericId = idValue;
      }

      // 创建一个新对象，确保字段名称统一
      const normalizedProject = {
        id: numericId,
        projectId: numericId,
        projectName: project.projectName || project.name || `项目 ${numericId}`,
        description: project.description || '',
        status: project.status || '进行中',
        startDate: project.startDate || '',
        endDate: project.endDate || ''
      };

      currentProject.value = normalizedProject;
      // 将当前项目保存到localStorage，以便在页面刷新后恢复
      localStorage.setItem('currentProject', JSON.stringify(normalizedProject));

      console.log("标准化后的项目对象:", normalizedProject);
    } else {
      currentProject.value = null;
      localStorage.removeItem('currentProject');
      console.warn("清除当前项目");
    }
  }

  // 初始化：从localStorage恢复项目状态
  const initProjectState = () => {
    console.log("尝试从localStorage恢复项目状态");

    const savedProject = localStorage.getItem('currentProject')
    console.log("localStorage中的项目数据:", savedProject);

    if (savedProject) {
      try {
        currentProject.value = JSON.parse(savedProject)
        console.log("从localStorage恢复的项目:", currentProject.value);
        console.log("从localStorage恢复的项目ID类型:", typeof currentProject.value.id, "值:", currentProject.value.id);

        // 确保项目对象格式完整
        if (currentProject.value) {
          const fixedProject = { ...currentProject.value };
          let needsUpdate = false;

          // 确保id和projectId存在且一致
          if (!fixedProject.id && fixedProject.projectId) {
            fixedProject.id = fixedProject.projectId;
            needsUpdate = true;
          } else if (!fixedProject.projectId && fixedProject.id) {
            fixedProject.projectId = fixedProject.id;
            needsUpdate = true;
          }

          // 确保projectName存在
          if (!fixedProject.projectName) {
            console.warn("恢复的项目数据缺少projectName字段:", fixedProject);
            if (fixedProject.name) {
              fixedProject.projectName = fixedProject.name;
            } else if (fixedProject.id) {
              fixedProject.projectName = `项目 ${fixedProject.id}`;
            }
            needsUpdate = true;
          }

          // 如果需要更新，保存修复后的数据
          if (needsUpdate) {
            currentProject.value = fixedProject;
            localStorage.setItem('currentProject', JSON.stringify(fixedProject));
            console.log("项目数据已修复:", fixedProject);
          }
        }
      } catch (e) {
        console.error('Failed to parse saved project:', e)
        localStorage.removeItem('currentProject')
        currentProject.value = null;
      }
    } else {
      console.log("localStorage中没有保存的项目数据");
      currentProject.value = null;
    }
  }

  // 检查是否已选择项目
  const hasSelectedProject = () => {
    return !!currentProject.value && !!(currentProject.value.id || currentProject.value.projectId);
  }

  // 获取当前项目ID
  const getCurrentProjectId = () => {
    // 尝试不同可能的项目ID字段
    let id = currentProject.value?.projectId || currentProject.value?.id || null;

    // 确保返回数字类型的ID（如果存在）
    if (id !== null) {
      // 将字符串转换为数字
      id = Number(id);
      if (isNaN(id)) {
        console.error("项目ID不是有效的数字:", currentProject.value);
        return null;
      }
    }

    console.log("获取当前项目ID (数字类型):", id);
    return id;
  }

  return {
    currentProject,
    setCurrentProject,
    initProjectState,
    hasSelectedProject,
    getCurrentProjectId
  }
}