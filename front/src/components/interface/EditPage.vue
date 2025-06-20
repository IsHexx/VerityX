<!-- EditPage.vue -->
<template>
  <div class="api-edit-page">
    <el-form
      ref="ruleFormRef"
      :model="ruleForm"
      :rules="rules"
      label-width="120px"
      class="api-form"
    >
      <!-- API编辑区域 -->
      <div class="api-edit-container">
        <!-- 路径和操作栏 -->
        <div class="api-path-bar">
          <div class="method-wrapper">
            <el-form-item prop="method" class="method-form-item">
              <el-select
                v-model="ruleForm.method"
                class="method-select"
                size="small"
              >
                <el-option label="GET" value="GET" />
                <el-option label="POST" value="POST" />
                <el-option label="PUT" value="PUT" />
                <el-option label="DELETE" value="DELETE" />
                <el-option label="PATCH" value="PATCH" />
              </el-select>
            </el-form-item>
          </div>

          <div class="path-input-wrapper">
            <el-form-item prop="path" class="path-form-item">
              <el-input
                v-model="ruleForm.path"
                placeholder="/api/path"
                size="small"
                class="api-path-input"
              />
            </el-form-item>
          </div>

          <div class="action-buttons">
            <el-button size="small" plain icon="Share">分享</el-button>
            <el-button size="small" plain :icon="VideoPlay">运行</el-button>
            <el-button type="primary" size="small" @click="onSubmit" :loading="saving">
              保存
            </el-button>
          </div>
        </div>

        <!-- 接口标题 -->
        <div class="api-title-section">
          <el-form-item prop="name" class="title-form-item">
            <el-input
              v-model="ruleForm.name"
              placeholder="接口名称"
              size="large"
              class="api-title-input"
            />
          </el-form-item>
          <div class="title-badges">
            <el-tag size="small" type="info">已保存</el-tag>
            <el-tag size="small" type="success">公开</el-tag>
          </div>
        </div>

        <!-- 说明区域 -->
        <div class="description-section">
          <div class="description-header">
            <span class="description-label">说明</span>
            <el-button
              size="small"
              text
              @click="toggleDescription"
              class="toggle-btn"
            >
              <el-icon>
                <component :is="descriptionExpanded ? 'ArrowUp' : 'ArrowDown'" />
              </el-icon>
            </el-button>
          </div>
          <div v-show="descriptionExpanded" class="description-content">
            <el-form-item prop="description">
              <el-input
                v-model="ruleForm.description"
                type="textarea"
                :rows="3"
                placeholder="生成房屋二维码接口"
                class="description-input"
                resize="vertical"
              />
            </el-form-item>
          </div>
        </div>

        <!-- 责任人区域 -->
        <div class="responsibility-section">
          <div class="responsibility-row">
            <div class="responsibility-item">
              <label class="item-label">责任人</label>
              <el-select v-model="ruleForm.owner" placeholder="请选择责任人" size="small" class="owner-select">
                <el-option label="张三" value="zhangsan" />
                <el-option label="李四" value="lisi" />
                <el-option label="王五" value="wangwu" />
              </el-select>
            </div>

            <div class="responsibility-item">
              <label class="item-label">标签</label>
              <el-input v-model="ruleForm.tags" placeholder="标签" size="small" class="tags-input" />
            </div>

            <div class="responsibility-item">
              <label class="item-label">前置 URL</label>
              <el-input v-model="ruleForm.preUrl" placeholder="请输入前置URL" size="small" class="pre-url-input" />
            </div>
          </div>

          <div class="additional-info">
            <el-button size="small" text icon="Plus">添加标签</el-button>
            <el-button size="small" text>高级模拟设置</el-button>
          </div>
        </div>
      </div>


    <!-- 请求参数 -->
    <div class="api-section">
      <div class="section-header">
        <h3 class="section-title">请求参数</h3>
      </div>

      <!-- 参数标签页 -->
      <div class="param-tabs">
        <el-tabs v-model="activeParamTab" class="param-tabs-container">
          <el-tab-pane label="Params" name="params">
            <!-- Query 参数 -->
            <div class="param-content">
              <div class="param-header">
                <span class="param-subtitle">Query 参数</span>
                <el-button size="small" type="primary" @click="addQueryParam">
                  + 添加参数
                </el-button>
              </div>
              <div class="param-table">
                <el-table :data="queryParams" style="width: 100%">
                  <el-table-column prop="name" label="参数名" width="150">
                    <template #default="scope">
                      <el-input v-model="scope.row.name" size="small" placeholder="houseId" />
                    </template>
                  </el-table-column>
                  <el-table-column prop="type" label="类型" width="100">
                    <template #default="scope">
                      <el-select v-model="scope.row.type" size="small">
                        <el-option label="string" value="string" />
                        <el-option label="number" value="number" />
                        <el-option label="boolean" value="boolean" />
                      </el-select>
                    </template>
                  </el-table-column>
                  <el-table-column prop="required" label="示例" width="100">
                    <template #default="scope">
                      <el-tag :type="scope.row.required ? 'danger' : 'info'" size="small">
                        {{ scope.row.required ? '必' : '*' }}
                      </el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="description" label="说明">
                    <template #default="scope">
                      <el-input v-model="scope.row.description" size="small" placeholder="房屋id" />
                    </template>
                  </el-table-column>
                  <el-table-column label="更多" width="80">
                    <template #default="scope">
                      <el-button size="small" text>更多</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>

              <!-- Path 参数 -->
              <div class="param-header" style="margin-top: 20px;">
                <span class="param-subtitle">Path 参数</span>
              </div>
              <div class="param-table">
                <el-table :data="pathParams" style="width: 100%">
                  <el-table-column prop="name" label="参数名" width="150">
                    <template #default="scope">
                      <span class="param-name">{{ scope.row.name }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="type" label="类型" width="100">
                    <template #default="scope">
                      <el-tag size="small" type="success">{{ scope.row.type }}</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="required" label="示例" width="100">
                    <template #default="scope">
                      <el-tag size="small" type="danger">必</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column prop="description" label="说明">
                    <template #default="scope">
                      <span>{{ scope.row.description }}</span>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="Body" name="body">
            <div class="param-content">
              <!-- Body类型选择 -->
              <div class="body-type-tabs">
                <el-radio-group v-model="bodyType" class="body-type-group">
                  <el-radio-button label="none">none</el-radio-button>
                  <el-radio-button label="form-data">form-data</el-radio-button>
                  <el-radio-button label="x-www-form-urlencoded">x-www-form-urlencoded</el-radio-button>
                  <el-radio-button label="json">json</el-radio-button>
                  <el-radio-button label="xml">xml</el-radio-button>  
                  <el-radio-button label="raw">raw</el-radio-button>
                  <el-radio-button label="binary">binary</el-radio-button>
                  <el-radio-button label="GraphQL">GraphQL</el-radio-button>
                  <el-radio-button label="msgpack">msgpack</el-radio-button>
                </el-radio-group>
              </div>

              <!-- Body内容区域 -->
              <div class="body-content">
                <div v-if="bodyType === 'none'" class="body-placeholder">
                  <p class="placeholder-text">没有选择 Body</p>
                </div>

                <div v-else-if="bodyType === 'form-data'" class="body-form-data">
                  <div class="param-header">
                    <span class="param-subtitle">Form Data 参数</span>
                    <el-button size="small" type="primary" @click="addFormDataParam">
                      + 添加参数
                    </el-button>
                  </div>
                  <div class="param-table">
                    <el-table :data="formDataParams" style="width: 100%">
                      <el-table-column prop="name" label="参数名" width="150">
                        <template #default="scope">
                          <el-input v-model="scope.row.name" size="small" placeholder="key" />
                        </template>
                      </el-table-column>
                      <el-table-column prop="type" label="类型" width="100">
                        <template #default="scope">
                          <el-select v-model="scope.row.type" size="small">
                            <el-option label="Text" value="text" />
                            <el-option label="File" value="file" />
                          </el-select>
                        </template>
                      </el-table-column>
                      <el-table-column prop="value" label="值">
                        <template #default="scope">
                          <el-input v-if="scope.row.type === 'text'" v-model="scope.row.value" size="small" placeholder="value" />
                          <el-upload v-else class="upload-demo" action="#" :auto-upload="false">
                            <el-button size="small">选择文件</el-button>
                          </el-upload>
                        </template>
                      </el-table-column>
                      <el-table-column prop="description" label="说明">
                        <template #default="scope">
                          <el-input v-model="scope.row.description" size="small" placeholder="说明" />
                        </template>
                      </el-table-column>
                      <el-table-column label="操作" width="80">
                        <template #default="scope">
                          <el-button
                            size="small"
                            type="danger"
                            @click="removeFormDataParam(scope.$index)"
                          >
                            删除
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </div>

                <div v-else-if="bodyType === 'x-www-form-urlencoded'" class="body-form-urlencoded">
                  <div class="param-header">
                    <span class="param-subtitle">URL Encoded 参数</span>
                    <el-button size="small" type="primary" @click="addUrlEncodedParam">
                      + 添加参数
                    </el-button>
                  </div>
                  <div class="param-table">
                    <el-table :data="urlEncodedParams" style="width: 100%">
                      <el-table-column prop="name" label="参数名" width="150">
                        <template #default="scope">
                          <el-input v-model="scope.row.name" size="small" placeholder="key" />
                        </template>
                      </el-table-column>
                      <el-table-column prop="value" label="值">
                        <template #default="scope">
                          <el-input v-model="scope.row.value" size="small" placeholder="value" />
                        </template>
                      </el-table-column>
                      <el-table-column prop="description" label="说明">
                        <template #default="scope">
                          <el-input v-model="scope.row.description" size="small" placeholder="说明" />
                        </template>
                      </el-table-column>
                      <el-table-column label="操作" width="80">
                        <template #default="scope">
                          <el-button
                            size="small"
                            type="danger"
                            @click="removeUrlEncodedParam(scope.$index)"
                          >
                            删除
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </div>

                <div v-else-if="bodyType === 'json'" class="body-json">
                  <div class="json-editor">
                    <el-input
                      v-model="jsonBody"
                      type="textarea"
                      :rows="10"
                      placeholder='{\n  "key": "value"\n}'
                      class="json-textarea"
                    />
                  </div>
                </div>

                <div v-else-if="bodyType === 'xml'" class="body-xml">
                  <div class="xml-editor">
                    <el-input
                      v-model="xmlBody"
                      type="textarea"
                      :rows="10"
                      placeholder='<?xml version="1.0" encoding="UTF-8"?>\n<root>\n  <element>value</element>\n</root>'
                      class="xml-textarea"
                    />
                  </div>
                </div>

                <div v-else-if="bodyType === 'raw'" class="body-raw">
                  <div class="raw-editor">
                    <el-input
                      v-model="rawBody"
                      type="textarea"
                      :rows="10"
                      placeholder="输入原始数据"
                      class="raw-textarea"
                    />
                  </div>
                </div>

                <div v-else-if="bodyType === 'binary'" class="body-binary">
                  <div class="binary-upload">
                    <el-upload
                      class="upload-demo"
                      drag
                      action="#"
                      :auto-upload="false"
                    >
                      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                      <div class="el-upload__text">
                        将文件拖到此处，或<em>点击上传</em>
                      </div>
                    </el-upload>
                  </div>
                </div>

                <div v-else-if="bodyType === 'GraphQL'" class="body-graphql">
                  <div class="graphql-editor">
                    <el-input
                      v-model="graphqlBody"
                      type="textarea"
                      :rows="10"
                      placeholder='query {\n  user(id: "1") {\n    name\n    email\n  }\n}'
                      class="graphql-textarea"
                    />
                  </div>
                </div>

                <div v-else class="body-placeholder">
                  <p class="placeholder-text">{{ bodyType }} 类型暂未实现</p>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="Headers" name="headers">
            <div class="param-content">
              <div class="param-header">
                <span class="param-subtitle">Header 参数</span>
                <el-button size="small" type="primary" @click="addHeaderParam">
                  + 添加参数
                </el-button>
              </div>
              <div class="param-table">
                <el-table :data="headerParams" style="width: 100%">
                  <el-table-column prop="name" label="参数名" width="150">
                    <template #default="scope">
                      <el-input v-model="scope.row.name" size="small" />
                    </template>
                  </el-table-column>
                  <el-table-column prop="value" label="参数值">
                    <template #default="scope">
                      <el-input v-model="scope.row.value" size="small" />
                    </template>
                  </el-table-column>
                  <el-table-column prop="required" label="必需" width="80">
                    <template #default="scope">
                      <el-checkbox v-model="scope.row.required" />
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="80">
                    <template #default="scope">
                      <el-button
                        size="small"
                        type="danger"
                        @click="removeHeaderParam(scope.$index)"
                      >
                        删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="Cookies" name="cookies">
            <div class="param-content">
              <div class="param-header">
                <span class="param-subtitle">Cookie 参数</span>
                <el-button size="small" type="primary" @click="addCookieParam">
                  + 添加参数
                </el-button>
              </div>
              <div class="param-table">
                <el-table :data="cookieParams" style="width: 100%">
                  <el-table-column prop="name" label="参数名" width="150">
                    <template #default="scope">
                      <el-input v-model="scope.row.name" size="small" placeholder="参数名" />
                    </template>
                  </el-table-column>
                  <el-table-column prop="type" label="类型" width="100">
                    <template #default="scope">
                      <el-select v-model="scope.row.type" size="small">
                        <el-option label="string" value="string" />
                        <el-option label="number" value="number" />
                        <el-option label="boolean" value="boolean" />
                      </el-select>
                    </template>
                  </el-table-column>
                  <el-table-column prop="example" label="示例值" width="120">
                    <template #default="scope">
                      <el-input v-model="scope.row.example" size="small" placeholder="示例值" />
                    </template>
                  </el-table-column>
                  <el-table-column prop="description" label="说明">
                    <template #default="scope">
                      <el-input v-model="scope.row.description" size="small" placeholder="说明" />
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="80">
                    <template #default="scope">
                      <el-button
                        size="small"
                        type="danger"
                        @click="removeCookieParam(scope.$index)"
                      >
                        删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>

              <!-- 空状态提示 -->
              <div v-if="cookieParams.length === 0" class="empty-state">
                <p class="empty-text">暂无Cookie参数</p>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="Auth" name="auth">
            <div class="param-content">
              <!-- 认证方式选择 -->
              <div class="auth-type-section">
                <div class="auth-type-header">
                  <span class="auth-type-label">认证方式</span>
                </div>
                <div class="auth-type-selector">
                  <el-select v-model="authType" placeholder="无需授权" size="default" class="auth-select">
                    <el-option label="无需授权" value="none" />
                    <el-option label="API Key" value="api-key" />
                    <el-option label="Bearer Token" value="bearer-token" />
                    <el-option label="JWT" value="jwt" />
                    <el-option label="Basic Auth" value="basic-auth" />
                    <el-option label="Digest Auth" value="digest-auth" />
                    <el-option label="OAuth 2.0" value="oauth-2.0" />
                    <el-option label="OAuth 1.0" value="oauth-1.0" />
                    <el-option label="Hawk Authentication" value="hawk-auth" />
                  </el-select>
                  <el-button size="small" text icon="View" class="auth-help-btn">
                    <el-icon><QuestionFilled /></el-icon>
                  </el-button>
                </div>
              </div>

              <!-- 认证配置区域 -->
              <div class="auth-config-section">
                <!-- 无需授权 -->
                <div v-if="authType === 'none'" class="auth-none">
                  <div class="auth-placeholder">
                    <el-icon class="auth-icon"><Lock /></el-icon>
                    <p class="auth-placeholder-text">此请求不使用任何授权</p>
                  </div>
                </div>

                <!-- API Key -->
                <div v-else-if="authType === 'api-key'" class="auth-api-key">
                  <div class="auth-form">
                    <div class="form-row">
                      <label class="form-label">Key</label>
                      <el-input v-model="authConfig.apiKey.key" placeholder="API Key名称" size="small" />
                    </div>
                    <div class="form-row">
                      <label class="form-label">Value</label>
                      <el-input v-model="authConfig.apiKey.value" placeholder="API Key值" size="small" type="password" show-password />
                    </div>
                    <div class="form-row">
                      <label class="form-label">Add to</label>
                      <el-select v-model="authConfig.apiKey.addTo" size="small">
                        <el-option label="Header" value="header" />
                        <el-option label="Query Params" value="query" />
                      </el-select>
                    </div>
                  </div>
                </div>

                <!-- Bearer Token -->
                <div v-else-if="authType === 'bearer-token'" class="auth-bearer">
                  <div class="auth-form">
                    <div class="form-row">
                      <label class="form-label">Token</label>
                      <el-input v-model="authConfig.bearerToken.token" placeholder="Bearer Token" size="small" type="password" show-password />
                    </div>
                  </div>
                </div>

                <!-- JWT -->
                <div v-else-if="authType === 'jwt'" class="auth-jwt">
                  <div class="auth-form">
                    <div class="form-row">
                      <label class="form-label">Token</label>
                      <el-input v-model="authConfig.jwt.token" placeholder="JWT Token" size="small" type="password" show-password />
                    </div>
                    <div class="form-row">
                      <label class="form-label">Algorithm</label>
                      <el-select v-model="authConfig.jwt.algorithm" size="small">
                        <el-option label="HS256" value="HS256" />
                        <el-option label="HS384" value="HS384" />
                        <el-option label="HS512" value="HS512" />
                        <el-option label="RS256" value="RS256" />
                      </el-select>
                    </div>
                  </div>
                </div>

                <!-- Basic Auth -->
                <div v-else-if="authType === 'basic-auth'" class="auth-basic">
                  <div class="auth-form">
                    <div class="form-row">
                      <label class="form-label">Username</label>
                      <el-input v-model="authConfig.basicAuth.username" placeholder="用户名" size="small" />
                    </div>
                    <div class="form-row">
                      <label class="form-label">Password</label>
                      <el-input v-model="authConfig.basicAuth.password" placeholder="密码" size="small" type="password" show-password />
                    </div>
                  </div>
                </div>

                <!-- Digest Auth -->
                <div v-else-if="authType === 'digest-auth'" class="auth-digest">
                  <div class="auth-form">
                    <div class="form-row">
                      <label class="form-label">Username</label>
                      <el-input v-model="authConfig.digestAuth.username" placeholder="用户名" size="small" />
                    </div>
                    <div class="form-row">
                      <label class="form-label">Password</label>
                      <el-input v-model="authConfig.digestAuth.password" placeholder="密码" size="small" type="password" show-password />
                    </div>
                    <div class="form-row">
                      <label class="form-label">Realm</label>
                      <el-input v-model="authConfig.digestAuth.realm" placeholder="Realm" size="small" />
                    </div>
                  </div>
                </div>

                <!-- OAuth 2.0 -->
                <div v-else-if="authType === 'oauth-2.0'" class="auth-oauth2">
                  <div class="auth-form">
                    <div class="form-row">
                      <label class="form-label">Grant Type</label>
                      <el-select v-model="authConfig.oauth2.grantType" size="small">
                        <el-option label="Authorization Code" value="authorization_code" />
                        <el-option label="Client Credentials" value="client_credentials" />
                        <el-option label="Password" value="password" />
                        <el-option label="Implicit" value="implicit" />
                      </el-select>
                    </div>
                    <div class="form-row">
                      <label class="form-label">Access Token URL</label>
                      <el-input v-model="authConfig.oauth2.accessTokenUrl" placeholder="Token URL" size="small" />
                    </div>
                    <div class="form-row">
                      <label class="form-label">Client ID</label>
                      <el-input v-model="authConfig.oauth2.clientId" placeholder="Client ID" size="small" />
                    </div>
                    <div class="form-row">
                      <label class="form-label">Client Secret</label>
                      <el-input v-model="authConfig.oauth2.clientSecret" placeholder="Client Secret" size="small" type="password" show-password />
                    </div>
                  </div>
                </div>

                <!-- OAuth 1.0 -->
                <div v-else-if="authType === 'oauth-1.0'" class="auth-oauth1">
                  <div class="auth-form">
                    <div class="form-row">
                      <label class="form-label">Consumer Key</label>
                      <el-input v-model="authConfig.oauth1.consumerKey" placeholder="Consumer Key" size="small" />
                    </div>
                    <div class="form-row">
                      <label class="form-label">Consumer Secret</label>
                      <el-input v-model="authConfig.oauth1.consumerSecret" placeholder="Consumer Secret" size="small" type="password" show-password />
                    </div>
                    <div class="form-row">
                      <label class="form-label">Token</label>
                      <el-input v-model="authConfig.oauth1.token" placeholder="Access Token" size="small" />
                    </div>
                    <div class="form-row">
                      <label class="form-label">Token Secret</label>
                      <el-input v-model="authConfig.oauth1.tokenSecret" placeholder="Token Secret" size="small" type="password" show-password />
                    </div>
                  </div>
                </div>

                <!-- Hawk Authentication -->
                <div v-else-if="authType === 'hawk-auth'" class="auth-hawk">
                  <div class="auth-form">
                    <div class="form-row">
                      <label class="form-label">Hawk ID</label>
                      <el-input v-model="authConfig.hawkAuth.id" placeholder="Hawk ID" size="small" />
                    </div>
                    <div class="form-row">
                      <label class="form-label">Hawk Key</label>
                      <el-input v-model="authConfig.hawkAuth.key" placeholder="Hawk Key" size="small" type="password" show-password />
                    </div>
                    <div class="form-row">
                      <label class="form-label">Algorithm</label>
                      <el-select v-model="authConfig.hawkAuth.algorithm" size="small">
                        <el-option label="sha1" value="sha1" />
                        <el-option label="sha256" value="sha256" />
                      </el-select>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 提示信息 -->
              <div class="auth-tips">
                <div class="tip-item">
                  <el-icon class="tip-icon"><InfoFilled /></el-icon>
                  <span class="tip-text">继承</span>
                  <span class="tip-desc">跟随父级目录的配置</span>
                </div>
                <div class="tip-item">
                  <el-icon class="tip-icon"><InfoFilled /></el-icon>
                  <span class="tip-text">鉴权组件</span>
                  <span class="tip-desc">复用鉴权组件，兼容 OpenAPI 规范</span>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="前置脚本" name="pre-script">
            <div class="param-content">
              <!-- 脚本编辑区域 -->
              <div class="script-editor-section">
                <div class="script-header">
                  <div class="script-title">
                    <span class="script-label">添加前置脚本</span>
                    <el-button size="small" text class="script-help-btn">
                      <el-icon><QuestionFilled /></el-icon>
                    </el-button>
                  </div>
                  <div class="script-actions">
                    <el-button size="small" @click="addPreScriptSnippet">添加脚本片段</el-button>
                  </div>
                </div>

                <!-- 脚本片段选择 -->
                <div class="script-snippets" v-if="showPreScriptSnippets">
                  <div class="snippets-section">
                    <div class="snippets-category">
                      <div class="category-header">
                        <el-icon><Document /></el-icon>
                        <span class="category-title">全局脚本 & 脚本片段</span>
                        <el-button size="small" text @click="togglePreScriptSnippets">
                          <el-icon><ArrowUp /></el-icon>
                        </el-button>
                      </div>
                      <div class="category-content">
                        <div class="snippet-item">
                          <el-icon><Document /></el-icon>
                          <span class="snippet-name">全局脚本</span>
                          <span class="snippet-desc">内置脚本</span>
                        </div>
                      </div>
                    </div>

                    <div class="snippets-description">
                      <p class="desc-text">
                        '全局脚本'是系统内置脚本，作用于：对接口请求参数进行统一处理（包括动态参数）的脚本解决方案或实际内容。
                      </p>
                      <ol class="desc-list">
                        <li>接口运营脚本：等脚本，需放在'全局脚本'之后，运行脚本不予做任何限制或约束接口运营对的数据；因则可取到的参数是'全局脚本之后'的脚本内容。</li>
                        <li>通过脚本 set 全局'等脚本，需要放在'全局脚本'之后，否则 脚本set 的全局'新不会对当前接口的请求参数生效。</li>
                      </ol>
                    </div>
                  </div>
                </div>

                <!-- 代码编辑器 -->
                <div class="code-editor">
                  <div class="editor-toolbar">
                    <div class="editor-info">
                      <span class="editor-lang">JavaScript</span>
                    </div>
                    <div class="editor-actions">
                      <el-button size="small" text>格式化</el-button>
                      <el-button size="small" text>全屏</el-button>
                    </div>
                  </div>
                  <div class="editor-content">
                    <el-input
                      v-model="preScript"
                      type="textarea"
                      :rows="15"
                      placeholder="// 在此编写前置脚本
// 示例：设置请求头
pm.request.headers.add({
  key: 'Authorization',
  value: 'Bearer ' + pm.environment.get('token')
});

// 示例：设置环境变量
pm.environment.set('timestamp', Date.now());"
                      class="script-textarea"
                      resize="vertical"
                    />
                  </div>
                </div>

                <!-- 脚本帮助信息 -->
                <div class="script-help">
                  <div class="help-section">
                    <h4 class="help-title">新增</h4>
                    <div class="help-items">
                      <div class="help-item">
                        <el-icon><Plus /></el-icon>
                        <span class="help-text">数据库操作</span>
                      </div>
                      <div class="help-item">
                        <el-icon><Document /></el-icon>
                        <span class="help-text">自定义脚本</span>
                      </div>
                      <div class="help-item">
                        <el-icon><Share /></el-icon>
                        <span class="help-text">公共脚本</span>
                      </div>
                      <div class="help-item">
                        <el-icon><Clock /></el-icon>
                        <span class="help-text">等待时间</span>
                      </div>
                    </div>
                  </div>

                  <div class="help-section">
                    <h4 class="help-title">导入</h4>
                    <div class="help-items">
                      <div class="help-item">
                        <el-icon><Download /></el-icon>
                        <span class="help-text">从其它项目/用例/自定义导入</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="后置脚本" name="post-script">
            <div class="param-content">
              <!-- 脚本编辑区域 -->
              <div class="script-editor-section">
                <div class="script-header">
                  <div class="script-title">
                    <span class="script-label">添加后置脚本</span>
                    <el-button size="small" text class="script-help-btn">
                      <el-icon><QuestionFilled /></el-icon>
                    </el-button>
                  </div>
                  <div class="script-actions">
                    <el-button size="small" @click="addPostScriptSnippet">添加脚本片段</el-button>
                  </div>
                </div>

                <!-- 脚本片段选择 -->
                <div class="script-snippets" v-if="showPostScriptSnippets">
                  <div class="snippets-section">
                    <div class="snippets-category">
                      <div class="category-header">
                        <span class="category-title">新增</span>
                        <el-button size="small" text @click="togglePostScriptSnippets">
                          <el-icon><ArrowUp /></el-icon>
                        </el-button>
                      </div>
                      <div class="category-content">
                        <div class="snippet-item" @click="addSnippet('断言')">
                          <el-icon class="snippet-icon error"><CircleClose /></el-icon>
                          <span class="snippet-name">断言</span>
                        </div>
                        <div class="snippet-item" @click="addSnippet('提取变量')">
                          <el-icon class="snippet-icon success"><Check /></el-icon>
                          <span class="snippet-name">提取变量</span>
                        </div>
                        <div class="snippet-item" @click="addSnippet('数据库操作')">
                          <el-icon class="snippet-icon primary"><DataBoard /></el-icon>
                          <span class="snippet-name">数据库操作</span>
                        </div>
                        <div class="snippet-item" @click="addSnippet('自定义脚本')">
                          <el-icon class="snippet-icon info"><Document /></el-icon>
                          <span class="snippet-name">自定义脚本</span>
                        </div>
                        <div class="snippet-item" @click="addSnippet('公共脚本')">
                          <el-icon class="snippet-icon warning"><Share /></el-icon>
                          <span class="snippet-name">公共脚本</span>
                        </div>
                        <div class="snippet-item" @click="addSnippet('等待时间')">
                          <el-icon class="snippet-icon info"><Clock /></el-icon>
                          <span class="snippet-name">等待时间</span>
                        </div>
                      </div>
                    </div>

                    <div class="snippets-category">
                      <div class="category-header">
                        <span class="category-title">导入</span>
                      </div>
                      <div class="category-content">
                        <div class="snippet-item" @click="importScript()">
                          <el-icon class="snippet-icon primary"><Download /></el-icon>
                          <span class="snippet-name">从其它项目/用例/自定义导入</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 代码编辑器 -->
                <div class="code-editor">
                  <div class="editor-toolbar">
                    <div class="editor-info">
                      <span class="editor-lang">JavaScript</span>
                    </div>
                    <div class="editor-actions">
                      <el-button size="small" text>格式化</el-button>
                      <el-button size="small" text>全屏</el-button>
                    </div>
                  </div>
                  <div class="editor-content">
                    <el-input
                      v-model="postScript"
                      type="textarea"
                      :rows="15"
                      placeholder="// 在此编写后置脚本
// 示例：验证响应状态
pm.test('Status code is 200', function () {
  pm.response.to.have.status(200);
});

// 示例：提取响应数据
var jsonData = pm.response.json();
pm.environment.set('userId', jsonData.data.id);

// 示例：断言响应内容
pm.test('Response has required fields', function () {
  pm.expect(jsonData).to.have.property('code');
  pm.expect(jsonData).to.have.property('data');
});"
                      class="script-textarea"
                      resize="vertical"
                    />
                  </div>
                </div>

                <!-- 脚本帮助信息 -->
                <div class="script-help">
                  <div class="help-section">
                    <h4 class="help-title">新增</h4>
                    <div class="help-items">
                      <div class="help-item">
                        <el-icon><Plus /></el-icon>
                        <span class="help-text">数据库操作</span>
                      </div>
                      <div class="help-item">
                        <el-icon><Document /></el-icon>
                        <span class="help-text">自定义脚本</span>
                      </div>
                      <div class="help-item">
                        <el-icon><Share /></el-icon>
                        <span class="help-text">公共脚本</span>
                      </div>
                      <div class="help-item">
                        <el-icon><Clock /></el-icon>
                        <span class="help-text">等待时间</span>
                      </div>
                    </div>
                  </div>

                  <div class="help-section">
                    <h4 class="help-title">导入</h4>
                    <div class="help-items">
                      <div class="help-item">
                        <el-icon><Download /></el-icon>
                        <span class="help-text">从其它项目/用例/自定义导入</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="设置" name="settings">
            <div class="param-content">
              <!-- 设置项列表 -->
              <div class="settings-section">
                <!-- SSL 证书验证 -->
                <div class="setting-item">
                  <div class="setting-left">
                    <span class="setting-label">SSL 证书验证</span>
                  </div>
                  <div class="setting-right">
                    <el-switch
                      v-model="settings.sslVerification"
                      size="default"
                      @change="onSettingChange('sslVerification')"
                    />
                    <el-button
                      size="small"
                      text
                      type="primary"
                      class="setting-link"
                      @click="manageCertificates"
                    >
                      证书管理
                    </el-button>
                  </div>
                </div>

                <!-- 自动跟随重定向 -->
                <div class="setting-item">
                  <div class="setting-left">
                    <span class="setting-label">自动跟随重定向</span>
                  </div>
                  <div class="setting-right">
                    <el-switch
                      v-model="settings.followRedirects"
                      size="default"
                      @change="onSettingChange('followRedirects')"
                    />
                    <el-button
                      size="small"
                      text
                      type="primary"
                      class="setting-link"
                      @click="configureRedirects"
                    >
                      通用设置
                    </el-button>
                  </div>
                </div>

                <!-- 兼容命令行的 JSON -->
                <div class="setting-item">
                  <div class="setting-left">
                    <span class="setting-label">兼容命令行的 JSON</span>
                  </div>
                  <div class="setting-right">
                    <el-switch
                      v-model="settings.compatibleJson"
                      size="default"
                      @change="onSettingChange('compatibleJson')"
                    />
                    <el-button
                      size="small"
                      text
                      type="primary"
                      class="setting-link"
                      @click="configureJsonSettings"
                    >
                      跳转项目设置
                    </el-button>
                  </div>
                </div>

                <!-- URL 自动编码 -->
                <div class="setting-item">
                  <div class="setting-left">
                    <span class="setting-label">URL 自动编码</span>
                  </div>
                  <div class="setting-right">
                    <el-button
                      size="small"
                      text
                      type="primary"
                      class="setting-link"
                      @click="configureUrlEncoding"
                    >
                      跳转项目设置
                    </el-button>
                  </div>
                </div>
              </div>

              <!-- 设置说明 -->
              <div class="settings-help">
                <div class="help-item">
                  <div class="help-icon">
                    <el-icon><InfoFilled /></el-icon>
                  </div>
                  <div class="help-content">
                    <h4 class="help-title">SSL 证书验证</h4>
                    <p class="help-desc">开启后将验证服务器的 SSL 证书有效性，关闭后将忽略证书错误</p>
                  </div>
                </div>

                <div class="help-item">
                  <div class="help-icon">
                    <el-icon><InfoFilled /></el-icon>
                  </div>
                  <div class="help-content">
                    <h4 class="help-title">自动跟随重定向</h4>
                    <p class="help-desc">开启后请求将自动跟随 HTTP 重定向响应（如 301、302 等）</p>
                  </div>
                </div>

                <div class="help-item">
                  <div class="help-icon">
                    <el-icon><InfoFilled /></el-icon>
                  </div>
                  <div class="help-content">
                    <h4 class="help-title">兼容命令行的 JSON</h4>
                    <p class="help-desc">开启后 JSON 格式将兼容命令行工具的输出格式</p>
                  </div>
                </div>

                <div class="help-item">
                  <div class="help-icon">
                    <el-icon><InfoFilled /></el-icon>
                  </div>
                  <div class="help-content">
                    <h4 class="help-title">URL 自动编码</h4>
                    <p class="help-desc">开启后将自动对 URL 中的特殊字符进行编码处理</p>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <!-- 返回响应 -->
    <div class="api-section">
      <h3 class="section-title">返回响应</h3>
      <div class="response-tabs">
        <el-button
          :type="responseActiveTab === '200' ? 'success' : ''"
          @click="responseActiveTab = '200'"
          size="small"
        >
          成功(200)
        </el-button>
        <el-button
          :type="responseActiveTab === '404' ? 'danger' : ''"
          @click="responseActiveTab = '404'"
          size="small"
        >
          404(404)
        </el-button>
      </div>

      <div class="response-info">
        <div class="response-meta">
          <span class="response-code">HTTP 状态码: {{ responseActiveTab }}</span>
          <span class="response-type">内容格式: JSON</span>
          <span class="response-content-type">application/json</span>
        </div>
      </div>

      <div class="response-body">
        <div class="response-header">
          <h4>数据结构</h4>
          <div class="response-actions">
            <el-button size="small">示例</el-button>
            <el-button size="small">字符串</el-button>
            <el-button size="small">原始</el-button>
          </div>
        </div>
        <div class="json-editor">
          <el-form-item prop="responseBody">
            <el-input
              v-model="ruleForm.responseBody"
              type="textarea"
              :rows="10"
              placeholder='{"message": "string"}'
              class="json-textarea"
            />
          </el-form-item>
        </div>
      </div>
    </div>
    </el-form>
  </div>
</template>
  
  <script setup>
import { reactive, ref, inject, watch, watchEffect, computed } from "vue";
import { ElMessage } from 'element-plus';
import { Document, ArrowUp, ArrowDown, Share, VideoPlay, QuestionFilled, Lock, InfoFilled, UploadFilled, Plus, Clock, Download, CircleClose, Check, DataBoard } from '@element-plus/icons-vue';
import { ApiManageApi } from "@/api/apiManageService";
  
const editApiData = inject('editApiData', null);
const syncApiData = inject('syncApiData', null);
const refreshTreeData = inject('refreshTreeData', null);

const formSize = ref("default");
const ruleFormRef = ref(null);
const saving = ref(false);
const descriptionExpanded = ref(true);

const ruleForm = reactive({
  name: '',
  method: 'GET',
  path: '',
  status: 'active',
  description: '',
  createdAt: '',
  updatedAt: '',
  mockUrl: '',
  responseBody: '{\n  "message": "string"\n}',
  apiDirectory: '', // 添加接口目录字段
  relatedTestCases: '', // 添加相关测试用例字段
  projectId: 1, // 添加项目ID字段
  owner: '', // 责任人
  tags: '', // 标签
  preUrl: '' // 前置URL
});

// Tab状态
const mockActiveTab = ref('mock');
const responseActiveTab = ref('200');
const activeParamTab = ref('params');

// Body相关状态
const bodyType = ref('none');
const jsonBody = ref('{\n  "key": "value"\n}');
const xmlBody = ref('<?xml version="1.0" encoding="UTF-8"?>\n<root>\n  <element>value</element>\n</root>');
const rawBody = ref('');
const graphqlBody = ref('query {\n  user(id: "1") {\n    name\n    email\n  }\n}');

// Body参数数据
const formDataParams = ref([]);
const urlEncodedParams = ref([]);

// Cookie参数数据
const cookieParams = ref([]);

// Auth认证相关
const authType = ref('none');
const authConfig = ref({
  apiKey: {
    key: '',
    value: '',
    addTo: 'header'
  },
  bearerToken: {
    token: ''
  },
  jwt: {
    token: '',
    algorithm: 'HS256'
  },
  basicAuth: {
    username: '',
    password: ''
  },
  digestAuth: {
    username: '',
    password: '',
    realm: ''
  },
  oauth2: {
    grantType: 'authorization_code',
    accessTokenUrl: '',
    clientId: '',
    clientSecret: ''
  },
  oauth1: {
    consumerKey: '',
    consumerSecret: '',
    token: '',
    tokenSecret: ''
  },
  hawkAuth: {
    id: '',
    key: '',
    algorithm: 'sha1'
  }
});

// 脚本相关
const preScript = ref('');
const postScript = ref('');
const showPreScriptSnippets = ref(false);
const showPostScriptSnippets = ref(false);

// 设置相关
const settings = ref({
  sslVerification: true,
  followRedirects: true,
  compatibleJson: false
});

// 从后端获取的真实数据
const mockData = ref([]);
const pathParams = ref([]);
const queryParams = ref([]);
const headerParams = ref([]);

// 接口详细信息
const apiDetail = ref(null);

// 表单验证规则
const rules = reactive({
  name: [
    { required: true, message: '请输入接口名称', trigger: 'blur' },
    { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
  ],
  method: [
    { required: true, message: '请选择请求方法', trigger: 'change' }
  ],
  path: [
    { required: true, message: '请输入接口路径', trigger: 'blur' },
    { pattern: /^\/.*/, message: '接口路径必须以 / 开头', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
});



// 监听 editApiData 变化
watch(
  () => editApiData?.value,
  (newData) => {
    if (newData) {
      console.log('编辑页数据更新:', newData);
      console.log('节点类型:', newData.type);
      console.log('节点ID:', newData.id);

      // 只有当选中的是接口类型时才获取详情
      if (newData.type === 'api' && typeof newData.id === 'number') {
        fetchApiDetails(newData.id);
      } else if (newData.type === 'api' && newData.apiData) {
        // 如果已经有接口数据，直接使用
        loadApiDataToForm(newData.apiData);
      } else {
        // 清空表单，但保留一些默认值
        clearForm();
        // 如果是目录节点，设置默认的apiDirectory
        if (newData.type === 'directory') {
          ruleForm.apiDirectory = newData.label || '';
        }
      }
    }
  },
  { immediate: true, deep: true }
);

// 获取 API 详情的方法
const fetchApiDetails = async (apiId) => {
  try {
    console.log('获取接口详情，ID:', apiId);
    const response = await ApiManageApi.getApiDetail(apiId);

    if (response && response.code === 200) {
      const apiDetail = response.data;
      loadApiDataToForm(apiDetail);
    } else {
      console.error('获取接口详情失败:', response);
      ElMessage.error('获取接口详情失败');
    }
  } catch (error) {
    console.error('获取接口详情失败', error);
    ElMessage.error('获取接口详情失败');
  }
};

// 将接口数据加载到表单
const loadApiDataToForm = (apiData) => {
  if (!apiData) return;

  console.log('加载接口数据到表单:', apiData);

  // 保存完整的接口详情
  apiDetail.value = apiData;

  // 基本信息 - 兼容新旧API格式
  ruleForm.name = apiData.name || apiData.apiName || '';
  ruleForm.method = apiData.method || apiData.requestMethod || 'GET';
  ruleForm.path = apiData.path || apiData.apiPath || '';
  ruleForm.status = apiData.status || 'active';
  ruleForm.description = apiData.description || '';
  ruleForm.createdAt = apiData.createdAt || '';
  ruleForm.updatedAt = apiData.updatedAt || '';
  ruleForm.mockUrl = apiData.mockUrl || '';
  ruleForm.responseBody = apiData.responseBody || '{\n  "message": "string"\n}';
  ruleForm.apiDirectory = apiData.apiDirectory || '';
  ruleForm.relatedTestCases = apiData.relatedTestCases || '';
  ruleForm.projectId = apiData.projectId || 1;

  // 新增字段
  ruleForm.owner = apiData.owner || '';
  ruleForm.tags = apiData.tags || '';
  ruleForm.preUrl = apiData.preUrl || '';

  // 确保editApiData中包含moduleId
  if (editApiData.value && apiData.moduleId) {
    editApiData.value.moduleId = apiData.moduleId;
    if (!editApiData.value.apiData) {
      editApiData.value.apiData = {};
    }
    editApiData.value.apiData.moduleId = apiData.moduleId;
  }

  // 解析参数数据（如果后端有提供的话）
  try {
    // 解析Path参数
    if (apiData.pathParams) {
      const pathParamsData = typeof apiData.pathParams === 'string'
        ? JSON.parse(apiData.pathParams)
        : apiData.pathParams;
      pathParams.value = Array.isArray(pathParamsData) ? pathParamsData : [];
    } else {
      pathParams.value = [];
    }

    // 解析Query参数
    if (apiData.queryParams) {
      const queryParamsData = typeof apiData.queryParams === 'string'
        ? JSON.parse(apiData.queryParams)
        : apiData.queryParams;
      queryParams.value = Array.isArray(queryParamsData) ? queryParamsData : [];
    } else {
      queryParams.value = [];
    }

    // 解析Header参数
    if (apiData.headerParams) {
      const headerParamsData = typeof apiData.headerParams === 'string'
        ? JSON.parse(apiData.headerParams)
        : apiData.headerParams;
      headerParams.value = Array.isArray(headerParamsData) ? headerParamsData : [];
    } else {
      headerParams.value = [];
    }

    // Mock数据处理
    if (apiData.mockData) {
      const mockDataParsed = typeof apiData.mockData === 'string'
        ? JSON.parse(apiData.mockData)
        : apiData.mockData;
      mockData.value = Array.isArray(mockDataParsed) ? mockDataParsed : [];
    } else {
      // 默认Mock数据
      mockData.value = [
        {
          name: '成功',
          code: '200',
          description: '本地Mock',
          url: ruleForm.mockUrl || 'WEB 端不支持手机 Mock，可使用云端 Mock'
        }
      ];
    }

    console.log('解析后的参数数据:');
    console.log('pathParams:', pathParams.value);
    console.log('queryParams:', queryParams.value);
    console.log('headerParams:', headerParams.value);
  } catch (error) {
    console.error('解析参数数据失败:', error);
    console.error('原始数据:', {
      pathParams: apiData.pathParams,
      queryParams: apiData.queryParams,
      headerParams: apiData.headerParams
    });
    // 使用默认值
    pathParams.value = [];
    queryParams.value = [];
    headerParams.value = [];
    mockData.value = [
      {
        name: '成功',
        code: '200',
        description: '本地Mock',
        url: ruleForm.mockUrl || 'WEB 端不支持手机 Mock，可使用云端 Mock'
      }
    ];
  }
};

// 获取基础URL
const getBaseUrl = () => {
  return 'http://localhost:8089';
};

// 切换说明展开/收起
const toggleDescription = () => {
  descriptionExpanded.value = !descriptionExpanded.value;
};

// 参数操作方法
const addQueryParam = () => {
  queryParams.value.push({
    name: '',
    type: 'string',
    description: '',
    required: false
  });
};

const removeQueryParam = (index) => {
  queryParams.value.splice(index, 1);
};

const addHeaderParam = () => {
  headerParams.value.push({
    name: '',
    value: '',
    required: false
  });
};

const removeHeaderParam = (index) => {
  headerParams.value.splice(index, 1);
};

// 添加Form Data参数
const addFormDataParam = () => {
  formDataParams.value.push({
    name: '',
    type: 'text',
    value: '',
    description: ''
  });
};

// 删除Form Data参数
const removeFormDataParam = (index) => {
  formDataParams.value.splice(index, 1);
};

// 添加URL Encoded参数
const addUrlEncodedParam = () => {
  urlEncodedParams.value.push({
    name: '',
    value: '',
    description: ''
  });
};

// 删除URL Encoded参数
const removeUrlEncodedParam = (index) => {
  urlEncodedParams.value.splice(index, 1);
};

// 添加Cookie参数
const addCookieParam = () => {
  cookieParams.value.push({
    name: '',
    type: 'string',
    example: '',
    description: ''
  });
};

// 删除Cookie参数
const removeCookieParam = (index) => {
  cookieParams.value.splice(index, 1);
};

// 脚本相关方法
const addPreScriptSnippet = () => {
  showPreScriptSnippets.value = !showPreScriptSnippets.value;
};

const togglePreScriptSnippets = () => {
  showPreScriptSnippets.value = false;
};

const addPostScriptSnippet = () => {
  showPostScriptSnippets.value = !showPostScriptSnippets.value;
};

const togglePostScriptSnippets = () => {
  showPostScriptSnippets.value = false;
};

// 添加脚本片段
const addSnippet = (type) => {
  let snippetCode = '';

  switch (type) {
    case '断言':
      snippetCode = `
// 断言响应状态码
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

// 断言响应时间
pm.test("Response time is less than 200ms", function () {
    pm.expect(pm.response.responseTime).to.be.below(200);
});

// 断言响应内容
pm.test("Response has required fields", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('code');
    pm.expect(jsonData).to.have.property('data');
});`;
      break;

    case '提取变量':
      snippetCode = `
// 提取响应数据到环境变量
var jsonData = pm.response.json();
pm.environment.set("userId", jsonData.data.id);
pm.environment.set("token", jsonData.data.token);

// 提取响应头
var contentType = pm.response.headers.get("Content-Type");
pm.environment.set("contentType", contentType);`;
      break;

    case '数据库操作':
      snippetCode = `
// 数据库查询示例
// 注意：需要配置数据库连接
/*
var query = "SELECT * FROM users WHERE id = ?";
var params = [pm.environment.get("userId")];
var result = pm.database.query(query, params);
pm.environment.set("userInfo", JSON.stringify(result));
*/`;
      break;

    case '自定义脚本':
      snippetCode = `
// 自定义脚本示例
function customFunction() {
    // 在这里编写自定义逻辑
    console.log("执行自定义脚本");
}

customFunction();`;
      break;

    case '公共脚本':
      snippetCode = `
// 调用公共脚本
// pm.globals.get("commonScript");`;
      break;

    case '等待时间':
      snippetCode = `
// 等待指定时间（毫秒）
setTimeout(function() {
    console.log("等待完成");
}, 1000);`;
      break;
  }

  if (snippetCode) {
    postScript.value += snippetCode + '\n\n';
  }

  showPostScriptSnippets.value = false;
};

// 导入脚本
const importScript = () => {
  console.log('导入脚本');
  showPostScriptSnippets.value = false;
};

// 设置相关方法
const onSettingChange = (settingKey) => {
  console.log(`设置 ${settingKey} 已更改为:`, settings.value[settingKey]);
  // 这里可以添加保存设置到后端的逻辑
};

const manageCertificates = () => {
  console.log('打开证书管理');
  // 这里可以打开证书管理对话框
};

const configureRedirects = () => {
  console.log('配置重定向设置');
  // 这里可以打开重定向配置页面
};

const configureJsonSettings = () => {
  console.log('配置 JSON 设置');
  // 这里可以跳转到项目设置页面
};

const configureUrlEncoding = () => {
  console.log('配置 URL 编码设置');
  // 这里可以跳转到项目设置页面
};

// 获取请求方法标签类型
const getMethodTagType = (method) => {
  const methodTypes = {
    'GET': 'success',
    'POST': 'primary',
    'PUT': 'warning',
    'DELETE': 'danger',
    'PATCH': 'info'
  };
  return methodTypes[method] || 'info';
};

// 清空表单
const clearForm = () => {
  // 清空接口详情
  apiDetail.value = null;

  // 清空表单数据
  ruleForm.name = '';
  ruleForm.method = 'GET';
  ruleForm.path = '';
  ruleForm.status = 'active';
  ruleForm.description = '';
  ruleForm.createdAt = '';
  ruleForm.updatedAt = '';
  ruleForm.mockUrl = '';
  ruleForm.responseBody = '{\n  "message": "string"\n}';
  ruleForm.apiDirectory = '';
  ruleForm.relatedTestCases = '';
  ruleForm.projectId = 1;
  ruleForm.owner = '';
  ruleForm.tags = '';
  ruleForm.preUrl = '';

  // 重置所有参数和数据
  pathParams.value = [];
  queryParams.value = [];
  headerParams.value = [];
  mockData.value = [];
};

  
// 保存接口信息
const onSubmit = async () => {
  saving.value = true;
  try {
    // 验证表单
    if (ruleFormRef.value) {
      await ruleFormRef.value.validate();
    } else {
      // 如果表单引用不存在，进行基本验证
      if (!ruleForm.name) {
        ElMessage.error('请输入接口名称');
        return;
      }
      if (!ruleForm.path) {
        ElMessage.error('请输入接口路径');
        return;
      }
    }

    if (!editApiData?.value?.id) {
      ElMessage.error('请先选择要编辑的接口');
      return;
    }

    // 构建更新数据 - 使用新的API字段名
    const updateData = {
      moduleId: editApiData.value?.apiData?.moduleId || editApiData.value?.moduleId,
      name: ruleForm.name,
      path: ruleForm.path,
      method: ruleForm.method,
      description: ruleForm.description,
      status: ruleForm.status,
      mockUrl: ruleForm.mockUrl,
      owner: ruleForm.owner,
      tags: ruleForm.tags,
      preUrl: ruleForm.preUrl,
      // 将参数数据转换为JSON字符串
      pathParams: JSON.stringify(pathParams.value || []),
      queryParams: JSON.stringify(queryParams.value || []),
      headerParams: JSON.stringify(headerParams.value || [])
    };

    console.log('保存接口数据:', updateData);

    // 调用API更新接口
    const response = await ApiManageApi.updateApi(editApiData.value.id, updateData);

    if (response && response.code === 200) {
      ElMessage.success('接口信息保存成功');

      // 获取更新后的接口详情
      try {
        const updatedResponse = await ApiManageApi.getApiDetail(editApiData.value.id);
        if (updatedResponse && updatedResponse.code === 200) {
          const updatedApiData = updatedResponse.data;

          // 更新本地数据
          if (editApiData.value.apiData) {
            Object.assign(editApiData.value.apiData, updatedApiData);
          }

          // 同步数据到预览页面
          if (syncApiData) {
            const syncData = {
              ...editApiData.value,
              apiData: updatedApiData
            };
            syncApiData(syncData);
          }

          // 刷新树数据以更新接口名称
          if (refreshTreeData) {
            await refreshTreeData();
          }
        }
      } catch (error) {
        console.error('获取更新后的接口详情失败:', error);
        // 即使获取详情失败，也要同步基本数据
        if (syncApiData && editApiData.value) {
          syncApiData(editApiData.value);
        }
        // 仍然刷新树数据
        if (refreshTreeData) {
          await refreshTreeData();
        }
      }
    } else {
      ElMessage.error('保存失败：' + (response?.message || '未知错误'));
    }
  } catch (error) {
    console.error('保存接口信息失败:', error);
    ElMessage.error('保存失败');
  } finally {
    saving.value = false;
  }
};
</script>

<style scoped>
.api-edit-page {
  min-height: 100%;
  background-color: #f8f9fa;
  overflow: visible; /* 允许内容自然展开 */
  padding: 0;
  min-width: 0; /* 允许收缩 */
}

.api-form {
  height: 100%;
}

/* API编辑容器 */
.api-edit-container {
  background: white;
  margin: 16px 24px 0 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 路径栏样式 */
.api-path-bar {
  background: white;
  padding: 12px 20px;
  border-bottom: 1px solid #e9ecef;
  display: flex;
  align-items: center;
  gap: 12px;
}

.method-wrapper {
  min-width: 60px;
}

.method-select {
  width: 60px;
}

.method-select :deep(.el-input__wrapper) {
  border: 1px solid #67c23a;
  border-radius: 4px;
  background: rgba(103, 194, 58, 0.1);
}

.method-select :deep(.el-input__inner) {
  font-weight: 600;
  font-size: 12px;
  color: #67c23a;
  text-align: center;
  padding: 4px 8px;
}

.path-display {
  flex: 1;
  margin: 0 12px;
}

.path-text {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 14px;
  color: #606266;
  background: #f5f7fa;
  padding: 6px 12px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  display: inline-block;
  min-width: 200px;
}



.method-form-item {
  margin-bottom: 0;
}

.method-form-item :deep(.el-form-item__label) {
  display: none;
}

.method-form-item :deep(.el-form-item__content) {
  margin-left: 0 !important;
}

.method-select {
  width: 80px;
}

.method-select :deep(.el-input__wrapper) {
  border: none;
  box-shadow: none;
  background: transparent;
}

.method-select :deep(.el-input__inner) {
  font-weight: 600;
  font-size: 14px;
  padding: 0;
  text-align: left;
}



.path-input-wrapper {
  flex: 1;
  margin: 0 16px;
}

.path-form-item {
  margin-bottom: 0;
}

.path-form-item :deep(.el-form-item__label) {
  display: none;
}

.path-form-item :deep(.el-form-item__content) {
  margin-left: 0 !important;
}

.api-path-input :deep(.el-input__wrapper) {
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  background: white;
}

.api-path-input :deep(.el-input__inner) {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 14px;
  color: #606266;
  padding: 8px 12px;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

/* 接口标题区域 */
.api-title-section {
  padding: 20px 24px 16px 24px;
  border-bottom: 1px solid #f0f2f5;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title-badges {
  display: flex;
  gap: 8px;
  margin-left: 16px;
}

/* 说明区域 */
.description-section {
  padding: 16px 24px;
  border-bottom: 1px solid #f0f2f5;
}

/* 责任人区域 */
.responsibility-section {
  padding: 16px 24px;
  background: #fafbfc;
}

.responsibility-row {
  display: flex;
  gap: 24px;
  align-items: center;
  margin-bottom: 12px;
}

.responsibility-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.item-label {
  font-size: 14px;
  color: #606266;
  min-width: 60px;
  text-align: right;
}

.owner-select {
  width: 120px;
}

.tags-input {
  width: 100px;
}

.pre-url-input {
  width: 200px;
}

.additional-info {
  display: flex;
  gap: 16px;
  align-items: center;
}

/* 参数标签页 */
.param-tabs {
  margin-top: 0;
}

.param-tabs-container :deep(.el-tabs__header) {
  margin: 0;
  border-bottom: 1px solid #e4e7ed;
}

.param-tabs-container :deep(.el-tabs__nav-wrap) {
  padding: 0 24px;
}

.param-tabs-container :deep(.el-tabs__item) {
  padding: 0 16px;
  height: 40px;
  line-height: 40px;
  font-size: 14px;
}

.param-content {
  padding: 20px 24px;
}

.param-subtitle {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.param-name {
  color: #409eff;
  font-weight: 500;
}

/* Body类型选择 */
.body-type-tabs {
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.body-type-group {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.body-type-group :deep(.el-radio-button__inner) {
  padding: 6px 12px;
  font-size: 12px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  background: white;
  color: #606266;
}

.body-type-group :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background: #409eff;
  border-color: #409eff;
  color: white;
}

.body-content {
  min-height: 200px;
}

.body-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 200px;
  background: #fafbfc;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
}

.placeholder-text {
  color: #909399;
  font-size: 14px;
  margin: 0;
}

/* JSON/XML/Raw编辑器 */
.json-editor,
.xml-editor,
.raw-editor,
.graphql-editor {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.json-textarea :deep(.el-textarea__inner),
.xml-textarea :deep(.el-textarea__inner),
.raw-textarea :deep(.el-textarea__inner),
.graphql-textarea :deep(.el-textarea__inner) {
  border: none;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 13px;
  line-height: 1.5;
  background: #fafbfc;
  resize: vertical;
}

/* Binary上传 */
.binary-upload {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 20px;
}

.binary-upload :deep(.el-upload-dragger) {
  border: 2px dashed #dcdfe6;
  border-radius: 4px;
  background: #fafbfc;
}

/* Form Data表格 */
.body-form-data .param-table,
.body-form-urlencoded .param-table {
  margin-top: 12px;
}

.upload-demo {
  display: inline-block;
}

.upload-demo :deep(.el-upload) {
  display: inline-block;
}

/* 空状态样式 */
.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 120px;
  background: #fafbfc;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
  margin-top: 16px;
}

.empty-text {
  color: #909399;
  font-size: 14px;
  margin: 0;
}

/* Auth认证样式 */
.auth-type-section {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.auth-type-header {
  margin-bottom: 12px;
}

.auth-type-label {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.auth-type-selector {
  display: flex;
  align-items: center;
  gap: 8px;
}

.auth-select {
  width: 200px;
}

.auth-help-btn {
  color: #909399;
}

.auth-config-section {
  margin-bottom: 24px;
}

.auth-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 120px;
  background: #fafbfc;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
}

.auth-icon {
  font-size: 32px;
  color: #c0c4cc;
  margin-bottom: 8px;
}

.auth-placeholder-text {
  color: #909399;
  font-size: 14px;
  margin: 0;
}

.auth-form {
  max-width: 400px;
}

.form-row {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  gap: 12px;
}

.form-label {
  font-size: 14px;
  color: #606266;
  min-width: 120px;
  text-align: right;
}

.form-row .el-input,
.form-row .el-select {
  flex: 1;
}

.auth-tips {
  background: #f0f9ff;
  border: 1px solid #b3d8ff;
  border-radius: 4px;
  padding: 16px;
}

.tip-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.tip-item:last-child {
  margin-bottom: 0;
}

.tip-icon {
  color: #409eff;
  font-size: 16px;
}

.tip-text {
  font-weight: 600;
  color: #303133;
  font-size: 14px;
}

.tip-desc {
  color: #606266;
  font-size: 14px;
}

/* 脚本编辑器样式 */
.script-editor-section {
  margin-top: 0;
}

.script-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e4e7ed;
}

.script-title {
  display: flex;
  align-items: center;
  gap: 8px;
}

.script-label {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.script-help-btn {
  color: #909399;
}

.script-actions {
  display: flex;
  gap: 8px;
}

/* 脚本片段区域 */
.script-snippets {
  margin-bottom: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background: #fafbfc;
}

.snippets-section {
  padding: 16px;
}

.snippets-category {
  margin-bottom: 16px;
}

.category-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  padding: 8px 12px;
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.category-title {
  flex: 1;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.category-content {
  padding-left: 24px;
}

.snippet-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  margin-bottom: 8px;
  cursor: pointer;
  transition: all 0.2s;
}

.snippet-item:hover {
  background: #f0f9ff;
  border-color: #409eff;
}

.snippet-icon {
  font-size: 16px;
}

.snippet-icon.error {
  color: #f56c6c;
}

.snippet-icon.success {
  color: #67c23a;
}

.snippet-icon.primary {
  color: #409eff;
}

.snippet-icon.info {
  color: #909399;
}

.snippet-icon.warning {
  color: #e6a23c;
}

.snippet-name {
  font-weight: 500;
  color: #303133;
}

.snippet-desc {
  color: #909399;
  font-size: 12px;
}

.snippets-description {
  background: white;
  padding: 16px;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

.desc-text {
  margin: 0 0 12px 0;
  color: #606266;
  font-size: 14px;
  line-height: 1.5;
}

.desc-list {
  margin: 0;
  padding-left: 20px;
  color: #606266;
  font-size: 14px;
  line-height: 1.6;
}

.desc-list li {
  margin-bottom: 8px;
}

/* 代码编辑器 */
.code-editor {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  margin-bottom: 20px;
}

.editor-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

.editor-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.editor-lang {
  font-size: 12px;
  color: #909399;
  background: #e4e7ed;
  padding: 2px 6px;
  border-radius: 2px;
}

.editor-actions {
  display: flex;
  gap: 8px;
}

.editor-content {
  padding: 0;
}

.script-textarea :deep(.el-textarea__inner) {
  border: none;
  border-radius: 0;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 13px;
  line-height: 1.5;
  background: #fafbfc;
  resize: vertical;
  padding: 16px;
}

/* 脚本帮助信息 */
.script-help {
  background: #f0f9ff;
  border: 1px solid #b3d8ff;
  border-radius: 4px;
  padding: 16px;
}

.help-section {
  margin-bottom: 16px;
}

.help-section:last-child {
  margin-bottom: 0;
}

.help-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 12px 0;
}

.help-items {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.help-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: white;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 13px;
  color: #606266;
}

.help-item .el-icon {
  color: #409eff;
}

/* 设置页面样式 */
.settings-section {
  margin-bottom: 32px;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-left {
  flex: 1;
}

.setting-label {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

.setting-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.setting-link {
  font-size: 13px;
  padding: 0;
  height: auto;
}

/* 设置帮助信息 */
.settings-help {
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  padding: 20px;
}

.settings-help .help-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 20px;
}

.settings-help .help-item:last-child {
  margin-bottom: 0;
}

.settings-help .help-icon {
  margin-top: 2px;
}

.settings-help .help-icon .el-icon {
  color: #409eff;
  font-size: 16px;
}

.settings-help .help-content {
  flex: 1;
}

.settings-help .help-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 6px 0;
}

.settings-help .help-desc {
  font-size: 13px;
  color: #606266;
  line-height: 1.5;
  margin: 0;
}

.title-form-item {
  margin-bottom: 0;
  flex: 1;
}

.title-form-item :deep(.el-form-item__label) {
  display: none;
}

.title-form-item :deep(.el-form-item__content) {
  margin-left: 0 !important;
}

.api-title-input :deep(.el-input__wrapper) {
  border: none;
  box-shadow: none;
  padding: 0;
  background: transparent;
}

.api-title-input :deep(.el-input__inner) {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  padding: 8px 0;
}

.api-title-input :deep(.el-input__inner::placeholder) {
  color: #c0c4cc;
  font-weight: 400;
}

.title-actions {
  display: flex;
  gap: 8px;
  margin-left: 16px;
}

/* 说明区域 */
.description-section {
  margin-top: 0;
}

.description-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.description-label {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.toggle-btn {
  padding: 4px;
  color: #909399;
  transition: color 0.3s;
}

.toggle-btn:hover {
  color: #409eff;
}

.description-content {
  margin-top: 12px;
}

.description-content .el-form-item {
  margin-bottom: 0;
}

.description-content .el-form-item :deep(.el-form-item__label) {
  display: none;
}

.description-content .el-form-item :deep(.el-form-item__content) {
  margin-left: 0 !important;
}

.description-input :deep(.el-textarea__inner) {
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  text-align: left;
  font-size: 14px;
  line-height: 1.6;
  padding: 12px;
  background: #fafbfc;
  transition: all 0.3s;
}

.description-input :deep(.el-textarea__inner):focus {
  background: white;
  border-color: #409eff;
}



/* 内容区域 */
.api-section {
  background: white;
  margin: 16px 24px;
  padding: 24px;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
  width: calc(100% - 48px);
  max-width: none;
  overflow-x: hidden;
  min-width: 0;
  box-sizing: border-box;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 16px 0;
  padding-bottom: 8px;
  border-bottom: 1px solid #e4e7ed;
}

.description-input :deep(.el-textarea__inner) {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  text-align: left;
}

/* 模拟数据区域 */
.mock-tabs {
  margin-bottom: 16px;
}

.mock-tabs .el-button {
  margin-right: 8px;
  border-radius: 4px;
}

.mock-table {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

/* 参数区域 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.param-section {
  margin-bottom: 24px;
}

.param-section:last-child {
  margin-bottom: 0;
}

.param-title {
  font-size: 14px;
  font-weight: 600;
  color: #606266;
  margin: 0 0 12px 0;
}

.param-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.param-table {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  overflow: visible; /* 移除表格滚动 */
}

.param-table :deep(.el-table) {
  border: none;
  width: 100%;
}

.param-table :deep(.el-table__header) {
  background-color: #f5f7fa;
}

.param-table :deep(.el-table td),
.param-table :deep(.el-table th) {
  border-bottom: 1px solid #e4e7ed;
  word-wrap: break-word; /* 允许文字换行 */
  overflow: visible;
}

/* 响应区域 */
.response-tabs {
  margin-bottom: 16px;
}

.response-tabs .el-button {
  margin-right: 8px;
  border-radius: 4px;
}

.response-info {
  margin-bottom: 16px;
}

.response-meta {
  display: flex;
  gap: 24px;
  padding: 12px 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
  font-size: 14px;
  color: #606266;
}

.response-code {
  font-weight: 600;
}

.response-body {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.response-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

.response-header h4 {
  margin: 0;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.response-actions {
  display: flex;
  gap: 8px;
}

.json-editor {
  padding: 16px;
}

.json-textarea :deep(.el-textarea__inner) {
  border: none;
  padding: 0;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 13px;
  line-height: 1.5;
  background-color: transparent;
  resize: vertical; /* 允许垂直调整大小 */
  min-height: 200px; /* 设置最小高度 */
  overflow: visible; /* 移除内部滚动 */
}

/* 表格内输入框样式 */
.param-table :deep(.el-input__wrapper) {
  border: none;
  box-shadow: none;
  padding: 4px 8px;
}

.param-table :deep(.el-select) {
  width: 100%;
}

.param-table :deep(.el-select .el-input__wrapper) {
  border: none;
  box-shadow: none;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .api-header {
    padding: 16px;
  }

  .api-section {
    margin: 12px 16px;
    padding: 16px;
  }

  .api-meta {
    flex-direction: column;
    align-items: stretch;
    gap: 8px;
  }

  .response-meta {
    flex-direction: column;
    gap: 8px;
  }

  .response-header {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }
}
</style>