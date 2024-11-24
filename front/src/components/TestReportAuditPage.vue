<template>
  <div>
    <el-card style="width: 100%">
      <template #header>
        <div class="card-header" >
          <span>基本信息</span>
        </div>
      </template>
      <el-form :model="form" label-width="auto" style="max-width: 600px">
        <el-form-item label="Activity name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="Activity zone">
          <el-select
            v-model="form.region"
            placeholder="please select your zone"
          >
            <el-option label="Zone one" value="shanghai" />
            <el-option label="Zone two" value="beijing" />
          </el-select>
        </el-form-item>
        <el-form-item label="Activity time">
          <el-col :span="11">
            <el-date-picker
              v-model="form.date1"
              type="date"
              placeholder="Pick a date"
              style="width: 100%"
            />
          </el-col>
          <el-col :span="2" class="text-center">
            <span class="text-gray-500">-</span>
          </el-col>
          <el-col :span="11">
            <el-time-picker
              v-model="form.date2"
              placeholder="Pick a time"
              style="width: 100%"
            />
          </el-col>
        </el-form-item>
        <el-form-item label="Instant delivery">
          <el-switch v-model="form.delivery" />
        </el-form-item>
        <el-form-item label="Activity type">
          <el-checkbox-group v-model="form.type">
            <el-checkbox value="Online activities" name="type">
              Online activities
            </el-checkbox>
            <el-checkbox value="Promotion activities" name="type">
              Promotion activities
            </el-checkbox>
            <el-checkbox value="Offline activities" name="type">
              Offline activities
            </el-checkbox>
            <el-checkbox value="Simple brand exposure" name="type">
              Simple brand exposure
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="Resources">
          <el-radio-group v-model="form.resource">
            <el-radio value="Sponsor">Sponsor</el-radio>
            <el-radio value="Venue">Venue</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Activity form">
          <el-input v-model="form.desc" type="textarea" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交</el-button>
          <el-button>取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card style="">
      <template #header>
        <div class="card-header">
          <span>关联的用例</span>
        </div>
      </template>
      <el-table :data="tableData" style="width: 100%">
          <el-table-column property="id" label="ID" width="80" />
          <el-table-column label="Date" width="120">
            <template #default="scope">{{ scope.row.date }}</template>
          </el-table-column>
          <el-table-column property="name" label="Name" width="120" />
          <el-table-column
            property="address"
            label="use show-overflow-tooltip"
            width="240"
            show-overflow-tooltip
          />
          <el-table-column property="address" label="address" />
        </el-table>
    </el-card>
    <el-card style="">
      <template #header>
        <div class="card-header">
          <span>遗留的BUG</span>
        </div>
      </template>
      <el-table :data="tableData" style="width: 100%">
          <el-table-column property="id" label="ID" width="80" />
          <el-table-column property="name" label="Name" width="120" />
          <el-table-column
            property="address"
            label="use show-overflow-tooltip"
            width="240"
            show-overflow-tooltip
          />
          <el-table-column property="address" label="address" />
        </el-table>
    </el-card>
    <el-card style="">
      <template #header>
        <div class="card-header">
          <span>报表</span>
        </div>
      </template>
      <div class="upper-content">
      <el-card style="width: 48%;">
      <template #header>
        <div class="card-header">
          <span>按用例统计</span>
        </div>
      </template>
      <div ref="pieChart" style="width: 100%; height: 300px;"></div>
    </el-card>
    <el-card style="width: 48%;">
      <template #header>
        <div class="card-header">
          <span>按测试类型统计</span>
        </div>
      </template>
      <el-table :data="tableData" style="width: 100%">
          <el-table-column property="id" label="ID" width="80" />
          <el-table-column property="name" label="Name" width="120" />
          <el-table-column property="address" label="address" />
      </el-table>
    </el-card>
  </div>
    </el-card>
    <el-card style="width: 100%">
        
        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交</el-button>
          <el-button>取消</el-button>
        </el-form-item>
    </el-card>
  </div>
</template>


<style scoped>
.upper-content {
  flex: 2;
  padding: 2px 20px 4px 20px;
  background-color: #f5f5f5;
  display: flex;
  justify-content: space-between;
}

</style>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElTable } from "element-plus";
import * as echarts from "echarts";

const pieChart = ref(null);


onMounted(() => {
  const myChart = echarts.init(pieChart.value);
  const option = {
    title: {
      text: '环形图示例',
      subtext: '测试数据',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '访问来源',
        type: 'pie',
        radius: ['50%', '70%'],
        avoidLabelOverlap: false,
        label: {
          show: true,
          position: 'outside'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '30',
            fontWeight: 'bold'
          }
        },
        data: [
          { value: 335, name: '直接访问' },
          { value: 310, name: '邮件营销' },
          { value: 234, name: '联盟广告' },
          { value: 135, name: '视频广告' },
          { value: 1548, name: '搜索引擎' }
        ],
        itemStyle: {
          // 自定义样式
        }
      }
    ]
  };
  myChart.setOption(option);
});

const tableData = [
  {
    id: "1",
    date: "2016-05-02",
    name: "Aleyna Kutzner",
    address: "Lohrbergstr. 86c, Süd Lilli, Saarland",
  },
  {
    id: "2",
    date: "2016-05-02",
    name: "Helen Jacobi",
    address: "760 A Street, South Frankfield, Illinois",
  },
  {
    id: "3",
    date: "2016-05-02",
    name: "Brandon Deckert",
    address: "Arnold-Ohletz-Str. 41a, Alt Malinascheid, Thüringen",
  },
  {
    id: "4",
    date: "2016-05-02",
    name: "Margie Smith",
    address: "23618 Windsor Drive, West Ricardoview, Idaho",
  },
];

// do not use same name with ref
const form = reactive({
  id: "",
  name: "",
  region: "",
  date1: "",
  date2: "",
  delivery: false,
  type: [],
  resource: "",
  desc: "",
});

const onSubmit = () => {
  console.log("submit!");
};
</script>