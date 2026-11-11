# GitHub Actions se APK aur AAB File Kaise Banaye aur Download Kare
# (How to Build and Download APK & AAB Files with GitHub Actions)

`Whiteboard India Edition` ke liye GitHub Actions workflow tayyar kar diya gaya hai (`.github/workflows/build-apk-aab.yml`). Iske jariye aap bina Android Studio install kiye, seedhe GitHub se **APK (Mobile me install karne ke liye)** aur **AAB (Google Play Store par upload karne ke liye)** files bana sakte hain.

---

## 🚀 Step 1: Code ko GitHub par Push karein (Export / Push)
1. AI Studio ke top-right ya settings menu se **Push to GitHub** ya project ko ZIP download karke apne GitHub repository me push karein.
2. Jab aap code ko `main` ya `master` branch me push karenge, to GitHub Actions **automatically** build shuru kar dega.

---

## ⚡ Step 2: Manual Trigger se APK aur AAB Banaye (Run Workflow)
Agar aap push kiye bina kabhi bhi naya APK/AAB generate karna chahte hain:
1. Apne GitHub repository me jayein.
2. Upar **"Actions"** tab par click karein.
3. Left sidebar me **"Build Whiteboard India APK and AAB"** workflow par click karein.
4. Right side me **"Run workflow"** button par click karein.
5. Select karein:
   - Branch: `main`
   - Build Type: `release` (ya `debug`)
6. Green **"Run workflow"** par click karein.

---

## 📦 Step 3: APK aur AAB File Download Kaise Karein?
1. **Actions** tab me chal rahe workflow run par click karein (e.g. *"Build APK and AAB"*).
2. Build complete hone par (green checkmark aayega).
3. Page ke bilkul neeche scroll karein, wahan **Artifacts** section dikhega:
   - **`Whiteboard-India-APK`**: Is zip file ko download karein, isme se `.apk` file nikaal kar seedhe kisi bhi Android phone ya tablet me install karke test karein.
   - **`Whiteboard-India-AAB`**: Is zip file ko download karein, isme `.aab` (Android App Bundle) file milegi jo Google Play Console par upload ki jati hai.

---

## 🏷️ Step 4: GitHub Release Banana (Optional)
Agar aap tag ke saath release banana chahte hain:
```bash
git tag v1.0.0
git push origin v1.0.0
```
Workflow automatically GitHub par **Release** bana dega aur APK + AAB files ko wahan attach kar dega!

---

## 🔐 Optional: Google Play Store Release Keystore (Production Signing)
Workflow me auto-signing ke liye fallback shaamil hai. Agar aapka apna custom Google Play Store Keystore hai, to GitHub repository ke:
**Settings > Secrets and variables > Actions > Repository secrets** me ye 4 secrets daal sakte hain:
1. `KEYSTORE_BASE64`: Aapki `.jks` file ka base64 encoded text (`base64 -w 0 my-release-key.jks`)
2. `STORE_PASSWORD`: Keystore password
3. `KEY_ALIAS`: Key alias
4. `KEY_PASSWORD`: Key password
