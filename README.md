# Data-Compression-Techniques-Information-Theory

## Data Compression Techniques - Implemented with Java

A comprehensive collection of five projects developed during the **Information Theory** course, exploring both **lossless and lossy data compression algorithms** for text and image data using **Java**.

---

## 📁 Contents

Each directory contains:
- Full Java implementation
- Input/output test cases
- Detailed reports
- Reconstructed images (for image compression)

| Folder Name                 | Technique Name                 | Compression Type | Domain     |
|----------------------------|--------------------------------|------------------|------------|
| `lz77-compression`         | LZ77                           | Lossless         | Text       |
| `lz78-compression`         | LZ78                           | Lossless         | Text       |
| `huffman-coding`           | Huffman Coding                 | Lossless         | Text       |
| `vector-quantization`      | Vector Quantization (LBG)      | Lossy            | Image      |
| `predictive-coding-dpcm`   | 2D Feed-Backward Predictive    | Lossy            | Image      |

---

## 🧠 Summary of Techniques

| Technique             | Type       | Domain        | Compression Type | Highlights                              |
|----------------------|------------|---------------|------------------|-----------------------------------------|
| LZ77                 | Lossless   | Text          | Dictionary-based | Uses sliding window                     |
| LZ78                 | Lossless   | Text          | Dictionary-based | Builds dynamic dictionary               |
| Huffman Coding       | Lossless   | Text          | Statistical      | Variable-length prefix codes            |
| Vector Quantization  | Lossy      | Image         | Transform-based  | Uses LBG (K-means variant)              |
| 2D Predictive Coding | Lossy      | Image         | Predictive       | Encodes residuals + quantization        |

---

## 📦 `lz77-compression`

### 📌 Description
Implements the **LZ77 lossless compression** algorithm. It replaces repeated text sequences with a reference to an earlier occurrence via a triplet `(offset, length, next char)`.

### 🔍 Algorithm
- Uses **sliding window buffers**:
  - **Search Buffer**: holds recent symbols
  - **Lookahead Buffer**: holds upcoming symbols
- Encodes repeating patterns as triplets.

### 📐 Compression Example
- **Original Size**: 64 bits  
- **Compressed Size**: 36 bits  
- **Compression Ratio**: 56.25%

### 📁 Key Files
```
LZ77Encoder.java
LZ77Decoder.java
TestCases.java
```

---

## 📦 `lz78-compression`

### 📌 Description
Java implementation of **LZ78**, where a **dictionary of substrings** is built and encoded as `(index, next character)` pairs.

### 🔍 Algorithm
- Dynamically builds dictionary as new substrings are encountered.
- Decoding reconstructs input by mapping indices.

### 📐 Compression Example
- **Input**: `ABCDABCABCDAABCABCE`
- **Original Size**: 152 bits  
- **Compressed Size**: 120 bits  
- **Compression Ratio**: 1.27×  

### 📁 Key Files
```
LZ78Encoder.java
LZ78Decoder.java
Dictionary.java
```

---

## 📦 `huffman-coding`

### 📌 Description
A Java implementation of **Huffman Coding**, a **variable-length prefix** coding scheme based on **symbol frequencies**.

### 🔍 Algorithm
1. Count frequency of characters.
2. Build a binary **Huffman Tree** using a priority queue.
3. Assign shorter binary codes to more frequent characters.

### 📐 Compression Results

#### Test Case 1
- **Original**: 128 bits  
- **Compressed**: 32 bits  
- **Entropy**: 1.91  
- **Average Code Length**: 2.0 bits/symbol

#### Test Case 2
- **Original**: 112 bits  
- **Compressed**: 34 bits  
- **Entropy**: 2.60  
- **Average Code Length**: 2.62 bits/symbol

### 📁 Key Files
```
HuffmanEncoder.java
HuffmanDecoder.java
HuffmanTree.java
```

---

## 🖼 `vector-quantization`

### 📌 Description
Implements **Vector Quantization (VQ)** using the **Linde-Buzo-Gray (LBG)** algorithm for compressing grayscale images.

### 🔍 Compression Pipeline
1. Convert image to **grayscale**
2. Divide into **blocks** (e.g., 4×4)
3. Convert each block to vector
4. Cluster vectors using **LBG** to form a **codebook**
5. Assign nearest codeword index during encoding
6. Reconstruct image from indices

### 📊 Results Table

| Block Size | Codebook Size | MSE     | Compression Ratio |
|------------|----------------|---------|-------------------|
| 4×4        | 32             | 27.033  | 25.42:1           |
| 2×2        | 32             | 12.594  | 6.40:1            |
| 8×8        | 8              | 87.974  | 163.54:1          |

### 🧠 Trade-offs
- **Higher block size → better compression, worse quality**
- **Higher codebook size → better quality, worse compression**

### 📁 Key Files
```
VQMain.java
LBGQuantizer.java
BlockDivider.java
ImageProcessor.java
VQMetrics.java
```

---

## 🖼 `predictive-coding-dpcm`

### 📌 Description
Java implementation of **2D Differential Pulse Code Modulation (DPCM)** using multiple predictors for image compression.

### 🔍 Predictors
- **Order-1**: Predicts from left neighbor
- **Order-2**: Predicts from left + top
- **Adaptive**: Uses min/max of surrounding pixels

### 🔍 Quantization Levels
- 8, 16, 32-level uniform quantization

### 📊 Results Table

| Predictor  | Q-Level | MSE     | Compression Ratio |
|------------|---------|---------|-------------------|
| Order-1    | 8       | 1281.15 | 2.67              |
|            | 16      | 301.53  | 2.00              |
|            | 32      | 64.79   | 1.60              |
| Order-2    | 8       | 1277.93 | 2.67              |
|            | 16      | 302.15  | 2.00              |
|            | 32      | 63.23   | 1.60              |
| Adaptive   | 8       | 1244.13 | 2.67              |
|            | 16      | 308.54  | 2.00              |
|            | 32      | 65.36   | 1.60              |

### 📁 Key Files
```
DPCMMain.java
Order1Predictor.java
Order2Predictor.java
AdaptivePredictor.java
Quantizer.java
ImageUtils.java
/reconstructed_images/
```

---

## 🚀 How to Run

1. **Install Java JDK 17+**  
   [Download here](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

2. **Compile and Run**  
   Navigate into the project directory and run:
   ```bash
   javac *.java
   java [MainClass]
   ```
---

## 📚 References

- [GeeksforGeeks – Huffman Coding](https://www.geeksforgeeks.org/huffman-coding-greedy-algo-3/)
- [YouTube – LZ77 Explained](https://youtu.be/PrbTpYVDv6Q)
- [Stringology – LZ78 Algorithm](https://www.stringology.org/DataCompression/lz78/index_en.html)
- [Java PriorityQueue API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/PriorityQueue.html)
- Gonzalez & Woods – *Digital Image Processing*
- Pratt – *Digital Image Processing*

---

