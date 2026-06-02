<div align="center">

# 🔐 license-key-internals

### A deep dive into how offline license keys actually work

[![Language](https://img.shields.io/badge/Language-Java%208-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com)
[![License](https://img.shields.io/badge/License-MIT-2E75B6?style=for-the-badge)](LICENSE)
[![Docs](https://img.shields.io/badge/Docs-English%20%2B%20සිංහල-1B3A5C?style=for-the-badge&logo=googledocs&logoColor=white)](#-documentation)
[![PRs Welcome](https://img.shields.io/badge/PRs-Welcome-C55A11?style=for-the-badge)](../../pulls)

<br/>

*No database. No server. No secret list of valid keys.*
*Just mathematics.*

</div>

---

## 🧭 What Is This?

This repository contains a **bilingual tutorial** — in **English** and **සිංහල (Sinhala)** — that walks through how offline license key generation and validation works, step by step.

Rather than just showing code, it answers the real questions:

- 🤔 **Why** does each step exist?
- 🎯 **What** does each method actually achieve?
- 🔄 **How** does a validator confirm a key without remembering it was ever generated?

The algorithm generates a 20-character key in the familiar format:

```
XXXX-XXXX-XXXX-XXXX-XXXX
```

...and the tutorial explains every decision that goes into building it.

---

## ✨ The Core Idea

> A valid key is not one that appears on a list.
> A valid key is one that **obeys a set of mathematical rules**.

The key carries its own proof of validity — embedded checksums that the validator can recompute independently. This is how Windows, Adobe, and countless other products have handled offline licensing for decades.

```
Generator:   random data  →  checksum  →  scramble  →  XXXX-XXXX-XXXX-XXXX-XXXX
Validator:   unscramble   →  recompute →  compare   →  ✅ VALID  or  ❌ INVALID
```

No shared secrets. No network calls. Pure mathematical self-consistency.

---

## 🗺️ How the Algorithm Works

The generation process runs through **9 distinct steps**:

| Step | What Happens |
|------|-------------|
| **1** | 🎲 Generate 20 random characters (digits + uppercase letters) |
| **2** | 🔀 Mix #1 — swap 7 specific position pairs |
| **3** | #️⃣ Hash the first 13 characters into a single integer |
| **4** | ⊕ XOR the hash with a constant for obfuscation |
| **5** | 🔡 Encode the integer into 7 characters (Pass 1) → write to positions 13–19 |
| **6** | 🔒 Run a CRC-32 style checksum over all 20 characters |
| **7** | 🔡 Encode the CRC into 7 characters (Pass 2) → overwrite positions 13–19 |
| **8** | 🔀 Mix #2 — the same shuffle again, scattering the checksum |
| **9** | 📦 Split into 5 × 4 blocks and format with dashes |

The **two-pass design** is the key insight: Pass 1 fills the verification region so the CRC in Step 6 has real data to work with. Pass 2 then overwrites it with the true integrity seal — making the final key depend on every single character.

---

## 📚 Documentation

The tutorial is available as a formatted Word document in two languages:

| Language | Document |
|----------|----------|
| 🇬🇧 **English** | [`LicenseKeyTutorial.docx`](./LicenseKeyTutorial.docx) |
| 🇱🇰 **සිංහල (Sinhala)** | [`LicenseKeyTutorial_Sinhala.docx`](./LicenseKeyTutorial_Sinhala.docx) |

Each document covers:

- 🏗️ **What we're building** — the big picture and core principle
- 🗺️ **The full generation flow** — all 9 steps at a glance
- 🔬 **Step-by-step breakdown** — each method explained: what, why, and what it achieves
- 🔁 **How validation works** — the mirror image of generation
- 📋 **Key concepts summary** — a reference table of all techniques used
- 🚀 **Production improvements** — SecureRandom, machine binding, expiry dates, validator protection

---

## 🔑 Key Concepts at a Glance

| Concept | Purpose |
|---------|---------|
| **Self-validating key** | The key contains embedded checksums; no external reference needed |
| **Permutation / Mix** | Fixed character swaps hide internal structure from inspection |
| **Initial Hash** | Adler-style modular hash of 13 data characters → 32-bit integer |
| **XOR Obfuscation** | Flips bits with a constant before encoding to obscure the hash |
| **5-bit Encoding** | Maps 32-bit integers to printable characters via custom alphabets |
| **CRC-32 Checksum** | Integrity seal computed over all 20 characters with a custom seed |
| **Two-pass Design** | Pass 1 fills positions 13–19; CRC runs; Pass 2 finalises them |
| **Human Formatting** | 5 × 4 grouping reduces transcription errors; dashes stripped on validation |

---

## 🚀 Running the Code

The source is written in **Java 8** with no external dependencies.

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/license-key-internals.git
cd license-key-internals

# Compile
javac LicenseKeyAlgorithm.java

# Run — generates 5 sample keys
java LicenseKeyAlgorithm
```

Sample output:

```
A7KM-NQ3X-P9RV-WZ4T-HJ2S
B2NP-XR8K-Q4MW-VZ7A-KT5H
M9QV-ZK3N-W7PR-TA2X-SH4J
...
```

---

## 🛡️ Production Considerations

This tutorial uses `java.util.Random` for simplicity. For real-world use:

- 🔐 Replace with `SecureRandom` — prevents key prediction from observed samples
- 👤 Bind keys to a user or machine — embed a hashed identifier in the data section
- 📅 Add expiry dates — encode year/month bytes that the validator checks
- 🛡️ Protect the validator — obfuscate bytecode; never put the check in one place

---

## 🤝 Contributing

Contributions are welcome — whether that's:

- 🌍 Translations into other languages
- 🧪 A companion validator implementation
- 📖 Additional explanations or diagrams
- 🐛 Bug fixes or code improvements

Please open an issue first to discuss what you'd like to change.

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

<div align="center">

*Built for learning. Explained for understanding. Written in two languages.*

**🇬🇧 English · 🇱🇰 සිංහල**

</div>
