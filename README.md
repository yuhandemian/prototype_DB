# prototype_DB

# 신경망의 순방향 및 역방향 계산

## 가정: 입력이 (0, 0)이고 목표 출력 T=1일 때

### 순방향 계산
1. **입력 레이어에서 은닉 레이어로 전달**
   - Z1 계산:
     \[
     Z1 = \begin{bmatrix} 0 \times 0.1 + 0 \times 0.3 + 0.1 & 0 \times 0.2 + 0 \times 0.4 + 0.2 \end{bmatrix} = \begin{bmatrix} 0.1 & 0.2 \end{bmatrix}
     \]
   - 활성화 함수 (시그모이드 함수) 적용:
     \[
     layer1 = sigmoid(Z1) = \begin{bmatrix} 0.52497919 & 0.549834 \end{bmatrix}
     \]

2. **은닉 레이어에서 출력 레이어로 전달**
   - Z2 계산:
     \[
     Z2 = \begin{bmatrix} 0.52497919 \times 0.5 + 0.549834 \times 0.6 + 0.3 \end{bmatrix} = \begin{bmatrix} 0.89238999 \end{bmatrix}
     \]
   - 활성화 함수 (시그모이드 함수) 적용:
     \[
     layer2 = sigmoid(Z2) = \begin{bmatrix} 0.70938314 \end{bmatrix}
     \]

### 역방향 계산
1. **출력층 오차 계산**
   \[
   layer2\_error = layer2 - T = \begin{bmatrix} 0.70938314 \end{bmatrix} - \begin{bmatrix} 1 \end{bmatrix} = \begin{bmatrix} -0.29061686 \end{bmatrix}
   \]

2. **출력층 델타 계산**
   - 활성화 함수의 도함수 계산:
     \[
     actf\_deriv(layer2) = layer2 \times (1 - layer2) = 0.70938314 \times (1 - 0.70938314) = 0.206158
     \]
   - 출력층 델타:
     \[
     layer2\_delta = layer2\_error \times actf\_deriv(layer2) = \begin{bmatrix} -0.29061686 \end{bmatrix} \times \begin{bmatrix} 0.206158 \end{bmatrix} = \begin{bmatrix} -0.059901 \end{bmatrix}
     \]

3. **은닉층 오차 계산**
   \[
   layer1\_error = layer2\_delta \cdot W2^T = \begin{bmatrix} -0.059901 \end{bmatrix} \cdot \begin{bmatrix} 0.50 & 0.60 \end{bmatrix} = \begin{bmatrix} -0.0299505 & -0.0359406 \end{bmatrix}
   \]

4. **은닉층 델타 계산**
   - 활성화 함수의 도함수 계산:
     \[
     actf\_deriv(layer1) = layer1 \times (1 - layer1) = \begin{bmatrix} 0.52497919 \times 0.47502081 & 0.549834 \times 0.450166 \end{bmatrix} = \begin{bmatrix} 0.249376 & 0.247516 \end{bmatrix}
     \]
   - 은닉층 델타:
     \[
     layer1\_delta = layer1\_error \times actf\_deriv(layer1) = \begin{bmatrix} -0.0299505 & -0.0359406 \end{bmatrix} \times \begin{bmatrix} 0.249376 & 0.247516 \end{bmatrix} = \begin{bmatrix} -0.007464 & -0.008891 \end{bmatrix}
     \]

### 가중치 및 편향 업데이트
1. **출력층 가중치 W2 업데이트**
   \[
   W2(t+1) = W2(t) - \eta \times (layer1^T \times layer2\_delta)
   \]
   \[
   \eta = 0.2, \quad layer1^T = \begin{bmatrix} 0.52497919 \\ 0.549834 \end{bmatrix}, \quad layer2\_delta = \begin{bmatrix} -0.059901 \end{bmatrix}
   \]
   \[
   W2(t+1) = \begin{bmatrix} 0.5 \\ 0.6 \end{bmatrix} + 0.2 \times \begin{bmatrix} 0.031442 \\ 0.032923 \end{bmatrix} = \begin{bmatrix} 0.506288 \\ 0.606585 \end{bmatrix}
   \]

2. **은닉층 가중치 W1 업데이트**
   \[
   W1(t+1) = W1(t) - \eta \times (layer0^T \times layer1\_delta)
   \]
   \[
   layer0^T = \begin{bmatrix} 0 & 0 \\ 0 & 0 \end{bmatrix}, \quad layer1\_delta = \begin{bmatrix} -0.007464 & -0.008891 \end{bmatrix}
   \]
   \[
   W1(t+1) = \begin{bmatrix} 0.1 & 0.3 \\ 0.2 & 0.4 \end{bmatrix}
   \]

3. **편향 B2 업데이트**
   \[
   B2(t+1) = B2(t) - \eta \times \sum(layer2\_delta, axis=0)
   \]
   \[
   B2(t+1) = 0.3 - 0.2 \times (-0.059901) = 0.3 + 0.0119802 = 0.3119802
   \]

4. **편향 B1 업데이트**
   \[
   B1(t+1) = B1(t) - \eta \times \sum(layer1\_delta, axis=0)
   \]
   \[
   B1(t+1) = 0.1 - 0.2 \times (-0.016355) = 0.1 + 0.003271 = 0.103271
   \]

## 예제 2: 입력이 (0, 1)이고 목표 출력 T=0일 때

### 순방향 계산
1. **입력 레이어에서 은닉 레이어로 전달**
   \[
   Z1 = \begin{bmatrix} 0 \times 0.1 + 1 \times 0.3 + 0.1 & 0 \times 0.2 + 1 \times 0.4 + 0.2 \end{bmatrix} = \begin{bmatrix} 0.4 & 0.6 \end{bmatrix}
   \]
   \[
   layer1 = sigmoid(Z1) = \begin{bmatrix} 0.59868766 & 0.64565631 \end{bmatrix}
   \]

2. **은닉 레이어에서 출력 레이어로 전달**
   \[
   Z2 = \begin{bmatrix} 0.59868766 \times 0.5 + 0.64565631 \times 0.6 + 0.3 \end{bmatrix} = \begin{bmatrix} 0.98673761 \end{bmatrix}
   \]
   \[
   layer2 = sigmoid(Z2) = \begin{bmatrix} 0.72844306 \end{bmatrix}
   \]

### 역방향 계산
1. **출력층 오차 계산**
   \[
   layer2\_error = layer2 - T = \begin{bmatrix} 0.72844306 \end{bmatrix} - \begin{bmatrix} 0 \end{bmatrix} = \begin{bmatrix} 0.72844306 \end{bmatrix}
   \]

2. **출력층 델타 계산**
   \[
   actf\_deriv(layer2) = layer2 \times (1 - layer2) = 0.72844306 \times (1 - 0.72844306) = 0.19781239
   \]
   \[
   layer2\_delta = layer2\_error \times actf\_deriv(layer2) = \begin{bmatrix} 0.72844306 \end{bmatrix} \times \begin{bmatrix} 0.19781239 \end{bmatrix} = \begin{bmatrix} 0.14409607 \end{bmatrix}
   \]

3. **은닉층 오차 계산**
   \[
   layer1\_error = layer2\_delta \cdot W2^T = \begin{bmatrix} 0.14409607 \end{bmatrix} \cdot \begin{bmatrix} 0.50 & 0.60 \end{bmatrix} = \begin{bmatrix} 0.07204804 & 0.08645764 \end{bmatrix}
   \]

4. **은닉층 델타 계산**
   \[
   actf\_deriv(layer1) = layer1 \times (1 - layer1) = \begin{bmatrix} 0.59868766 \times 0.40131234 & 0.64565631 \times 0.35434369 \end{bmatrix} = \begin{bmatrix} 0.239983 & 0.229639 \end{bmatrix}
   \]
   \[
   layer1\_delta = layer1\_error \times actf\_deriv(layer1) = \begin{bmatrix} 0.07204804 & 0.08645764 \end{bmatrix} \times \begin{bmatrix} 0.239983 & 0.229639 \end{bmatrix} = \begin{bmatrix} 0.01731031 & 0.01985518 \end{bmatrix}
   \]

### 가중치 및 편향 업데이트
1. **출력층 가중치 W2 업데이트**
   \[
   W2(t+1) = W2(t) - \eta \times (layer1^T \times layer2\_delta)
   \]
   \[
   layer1^T = \begin{bmatrix} 0.59868766 \\ 0.64565631 \end{bmatrix}, \quad layer2\_delta = \begin{bmatrix} 0.14409607 \end{bmatrix}
   \]
   \[
   W2(t+1) = \begin{bmatrix} 0.5 \\ 0.6 \end{bmatrix} - 0.2 \times \begin{bmatrix} 0.08633015 \\ 0.09314022 \end{bmatrix} = \begin{bmatrix} 0.48268969 \\ 0.58014482 \end{bmatrix}
   \]

2. **은닉층 가중치 W1 업데이트**
   \[
   W1(t+1) = W1(t) - \eta \times (layer0^T \times layer1\_delta)
   \]
   \[
   layer0^T = \begin{bmatrix} 0 & 1 \\ 0 & 1 \end{bmatrix}, \quad layer1\_delta = \begin{bmatrix} 0.01731031 & 0.01985518 \end{bmatrix}
   \]
   \[
   W1(t+1) = \begin{bmatrix} 0.1 & 0.3 \\ 0.2 & 0.4 \end{bmatrix} - 0.2 \times \begin{bmatrix} 0 & 0.01731031 \\ 0 & 0.01985518 \end{bmatrix} = \begin{bmatrix} 0.1 & 0.29653806 \\ 0.2 & 0.39602904 \end{bmatrix}
   \]

3. **편향 B2 업데이트**
   \[
   B2(t+1) = B2(t) - \eta \times \sum(layer2\_delta, axis=0)
   \]
   \[
   B2(t+1) = 0.3 - 0.2 \times 0.14409607 = 0.271181
   \]

4. **편향 B1 업데이트**
   \[
   B1(t+1) = B1(t) - \eta \times \sum(layer1\_delta, axis=0)
   \]
   \[
   B1(t+1) = 0.1 - 0.2 \times \begin{bmatrix} 0.01731031 & 0.01985518 \end{bmatrix} = \begin{bmatrix} 0.09653806 & 0.19602904 \end{bmatrix}
   \]

## 다른 예제들
마찬가지로 입력이 (1,0)이고 목표 출력이 T=0일 때와 입력이 (1,1)이고 목표 출력이 T=1일 때도 위와 같은 순서로 계산을 진행합니다.

## 결론
위의 순방향 및 역방향 계산 과정을 통해 신경망의 가중치와 편향이 업데이트되는 과정을 확인할 수 있습니다. 이를 통해 신경망이 학습하고 목표 출력에 더 가까워지도록 조정됩니다.
