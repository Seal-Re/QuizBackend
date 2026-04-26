<template>
  <div class="main-container">
    <div class="content-card" v-if="Flag === 0">
      <header class="header">
        <h1 class="page-title">Vidar知识大挑战</h1>
      </header>
      <div class="question-content">
        <el-card v-if="currentQuestion" class="question-box">
          <template #header>
            <div class="question-header">
              <h3 class="question-text">{{ currentQuestion.question }}</h3>
            </div>
          </template>
          <el-radio-group v-model="selectedOption" class="custom-radio-group" :disabled="showDetails">
            <el-radio
              v-for="(option, index) in currentQuestion.options"
              :key="index"
              :label="option"
              class="custom-radio"
            >
              {{ option }}
            </el-radio>
          </el-radio-group>
          <template v-if="showDetails">
            <div class="correct-answer-container">
              <p class="correct-answer-text">正确答案: <span class="correct-answer-value">{{ currentQuestion.answer }}</span></p>
            </div>
          </template>
        </el-card>
        <div v-else class="loading-state">
          <span>加载中...</span>
        </div>
      </div>
    </div>
    <div v-else class="result-page-container">
      <div class="result-box">
        <h1 class="score-text">{{ score_view }}分</h1>
        <el-button type="primary" class="result-button" @click="showQuestionDetails">
          查看题目详情
        </el-button>
        <el-button type="primary" class="result-button" @click="goBackHome">
          返回主页
        </el-button>
      </div>
    </div>
    
    <aside class="sidebar-container" v-show="Flag === 0">
      <el-card class="sidebar-card">
        <template #header>
          <div class="sidebar-header">
            <h3>题目列表</h3>
          </div>
        </template>
        <div class="question-list-grid">
          <div
            v-for="(item, index) in questionList"
            :key="index"
            class="question-number"
            :class="{
              'is-active': index === currentIndex,
              'is-answered': answerRecords[index] !== ''
            }"
            @click="changeCurrentQuestion(index)"
          >
            {{ index + 1 }}
          </div>
        </div>
      </el-card>
      <div class="navigation-buttons">
        <el-button type="primary" class="nav-button" @click="prevQuestion" :disabled="currentIndex === 0 || showDetails">
          上一题
        </el-button>
        <el-button type="primary" class="nav-button" @click="nextQuestion" :disabled="showDetails">
          {{ currentIndex === questionList.length - 1 ? '提交' : '下一题' }}
        </el-button>
      </div>
    </aside>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { usequestion_trainStore } from '@/store/index';
import router from '@/router/index';

const questionStore = usequestion_trainStore();

const processQuestionData = (data) => {
  return data?.questions && Array.isArray(data.questions) ? data.questions : [];
};

const questionList = ref([]);
const currentIndex = ref(0);
const selectedOption = ref('');
const answerRecords = ref([]);
const answerResults = ref([]);
const isLoading = ref(true);
const score = ref(0);
const score_view = ref(0);
const Flag = ref(0);
const showDetails = ref(false);

const currentQuestion = computed(() => questionList.value[currentIndex.value]);

const showQuestionDetails = () => {
  showDetails.value = true;
  currentIndex.value = 0;
  Flag.value = 0;
  selectedOption.value = answerRecords.value[currentIndex.value] || '';
};

const goBackHome = () => {
  router.push('/home');
  showDetails.value = false;
  questionStore.resetState();
};

const nextQuestion = () => {
  if (currentIndex.value < questionList.value.length - 1) {
    answerRecords.value[currentIndex.value] = selectedOption.value;
    currentIndex.value++;
    selectedOption.value = answerRecords.value[currentIndex.value] || '';
  } else {
    // 提交答案
    submitAnswers();
  }
};

const prevQuestion = () => {
  if (currentIndex.value > 0) {
    answerRecords.value[currentIndex.value] = selectedOption.value;
    currentIndex.value--;
    selectedOption.value = answerRecords.value[currentIndex.value] || '';
  }
};

const changeCurrentQuestion = (index) => {
  if (!showDetails.value) {
    answerRecords.value[currentIndex.value] = selectedOption.value;
    currentIndex.value = index;
    selectedOption.value = answerRecords.value[currentIndex.value] || '';
  }
};

const submitAnswers = () => {
  // 保存最后一题的答案
  answerRecords.value[currentIndex.value] = selectedOption.value;
  // 计算得分
  let finalScore = 0;
  const results = Array(questionList.value.length).fill(false);
  questionList.value.forEach((question, index) => {
    const isCorrect = answerRecords.value[index] === question.answer;
    results[index] = isCorrect;
    if (isCorrect) {
      finalScore += parseInt(question.score, 10);
    }
  });
  score.value = finalScore;
  score_view.value = score.value;
  answerResults.value = results;
  Flag.value = 1;
};

onMounted(async () => {
  try {
    const storeData = processQuestionData(questionStore.questionList);
    if (storeData.length === 0) {
      const response = await axios.get(`/api/questions`);
      const processedData = processQuestionData(response.data);
      questionList.value = processedData.filter(q => q.model === '0');
      answerRecords.value = Array(questionList.value.length).fill('');
    } else {
      questionList.value = storeData.filter(q => q.model === '0');
      answerRecords.value = questionStore.answerRecords;
    }
    isLoading.value = false;
  } catch (error) {
    console.error('获取题目数据失败:', error);
    isLoading.value = false;
  }
});
</script>

<style scoped>
.main-container {
  display: flex;
  justify-content: center;
  gap: 20px;
  padding: 40px;
  background-color: #f0f2f5;
  min-height: 100vh;
}

.content-card {
  flex: 3;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 30px;
}

.header {
  text-align: center;
  margin-bottom: 20px;
}

.page-title {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.question-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.question-box {
  width: 100%;
  border: none;
  box-shadow: none;
}

.question-header {
  text-align: center;
}

.question-text {
  font-size: 20px;
  font-weight: bold;
  color: #555;
  line-height: 1.5;
}

.custom-radio-group {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-top: 20px;
}

.custom-radio {
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  padding: 15px 20px;
  transition: all 0.3s ease;
  width: 100%;
}

.custom-radio:hover {
  border-color: #409eff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.custom-radio.is-checked {
  background-color: #ecf5ff;
  border-color: #409eff;
  color: #409eff;
}

.loading-state {
  text-align: center;
  font-size: 18px;
  color: #999;
  padding: 50px 0;
}

.correct-answer-container {
  margin-top: 20px;
  padding: 15px;
  background-color: #f0f9eb;
  border: 1px solid #e1f3d8;
  border-radius: 8px;
}

.correct-answer-text {
  font-size: 16px;
  color: #67c23a;
  font-weight: bold;
}

.correct-answer-value {
  color: #409eff;
}

.sidebar-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.sidebar-card {
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.sidebar-header {
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.question-list-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(40px, 1fr));
  gap: 10px;
  justify-content: center;
}

.question-number {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #f2f2f2;
  color: #555;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
}

.question-number:hover {
  background-color: #e6e6e6;
}

.question-number.is-active {
  background-color: #409eff;
  color: #ffffff;
}

.question-number.is-answered {
  background-color: #67c23a;
  color: #ffffff;
}

.navigation-buttons {
  display: flex;
  gap: 15px;
  margin-top: 20px;
}

.nav-button {
  flex: 1;
  height: 50px;
  font-size: 16px;
  font-weight: bold;
  border-radius: 8px;
}

.result-page-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100%;
}

.result-box {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 60px 40px;
  text-align: center;
  box-sizing: border-box;
}

.score-text {
  font-size: 60px;
  font-weight: 700;
  color: #007bff;
  margin-bottom: 40px;
}

.result-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 240px;
  height: 50px;
  margin: 20px auto;
  font-size: 18px;
  border-radius: 8px;
  transition: background-color 0.2s ease;
}

.result-button:hover {
  background-color: #007bff;
}

@media (max-width: 768px) {
  .main-container {
    flex-direction: column;
    padding: 20px;
  }
}
</style>