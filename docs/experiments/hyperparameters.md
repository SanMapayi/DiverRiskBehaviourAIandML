# Hyperparameter Configuration

This document describes the final hyperparameters used in the deep learning model designed to predict risky driving behaviour.

---

## 1. Optimisation Settings

| Parameter | Value | Description |
|----------|------|-------------|
| Optimiser | Adam | Adaptive learning rate optimiser |
| Learning Rate | 0.001 | Step size used for gradient updates |
| Weight Initialisation | Xavier | Prevents vanishing/exploding gradients |

---

## 2. Model Architecture

| Layer | Type | Units | Activation |
|------|------|------|------------|
| Input | Dense | 8 | – |
| Hidden Layer 1 | Dense | 32 | ReLU |
| Hidden Layer 2 | Dense | 16 | ReLU |
| Output | Dense | 2 | Softmax |

---

## 3. Regularisation

| Parameter | Value | Description |
|----------|------|-------------|
| Dropout | Not used | Dataset size sufficient |
| L2 Regularisation | 0.0001 | Reduces overfitting |

---

## 4. Training Parameters

| Parameter | Value | Description |
|----------|------|-------------|
| Batch Size | 64 | Number of samples per gradient update |
| Epochs | 10–50 | Number of full passes through training data |
| Early Stopping | Not implemented | Full epochs used |

---

## 5. Class Imbalance Handling

To address skewed class distribution, weighted loss functions were implemented:

| Class | Weight |
|------|--------|
| Safe Driving (0) | 1.0 |
| Risky Driving (1) | 8.0 |

This ensured higher penalty when risky driving examples were misclassified.

---

## 6. Data Preprocessing

| Step | Description |
|------|------------|
| Normalisation | Z-score standardisation |
| Feature Selection | Driving behavioural metrics only |
| Metadata Exclusion | Driver ID and Trip ID excluded from training |

---

## 7. Evaluation Strategy

The model was evaluated using:

- Accuracy
- Precision (minority class)
- Recall (minority class)
- F1-score
- Confusion Matrix

Evaluation outputs were saved to:

docs/Experiments/results.csv
docs/Experiments/loss.csv
docs/Experiments/confusion-matrix.csv


---

## 8. Experimental Notes

All experiments were executed using a fixed train-test split to ensure reproducibility. The model was retrained using weighted loss functions to mitigate class imbalance effects.
