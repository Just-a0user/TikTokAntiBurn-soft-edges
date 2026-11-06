<!-- Fork notes (added) -->
## TikTok AntiBurn – Soft Edge fork

Unofficial fork of [0mnr0/TikTokAntiBurn](https://github.com/0mnr0/TikTokAntiBurn). It covers fixed TikTok UI elements with black overlays to help prevent AMOLED screen burn-in.

### What's new in this fork
- **Soft edges, per block:** every block now has its own *Soft edge* slider in the edit menu (0 = off, up to 30 dp). The black area keeps its exact size, the fade is added *outside* of it, so there is no visible hard line between dimmed and undimmed areas. A global default can be set in *Additional settings* and is used for blocks without their own value.
- **Solid black blocks:** move the *Alpha* slider to the far right end (“100% – solid black”). Android 12+ limits normal touch-through overlays to about 80% opacity. Solid blocks are shown through the accessibility service instead, which allows fully black pixels while taps still pass through. If the accessibility service is not running, the block falls back to a normal overlay that blocks touches.
- GitHub Actions workflow that builds the APK.

### Install
1. Uninstall the original TikTokAntiBurn first (different signing key). Note down your block positions, settings are lost.
2. Download `TikTokAntiBurn-soft-edge.apk` below and install it. Google Play Protect may block it (sideloaded app that uses accessibility). Either install it via `adb install`, or temporarily turn off *Scan apps with Play Protect*.
3. Grant the overlay and accessibility permissions. If accessibility is greyed out: App info → ⋮ → *Allow restricted settings*.
4. Open *Additional settings* for the global soft edge default, and select a block in the editor for its own soft edge and alpha.

### ⚠️ Disclaimer – use at your own risk
This software is provided **“as is”, without warranty of any kind**. **Use it entirely at your own risk.** The author of this fork is **not responsible** for anything that happens when you use it, including (but not limited to) damage to your device or display, data loss, burn-in that is not prevented or not repaired, battery or performance issues, security or privacy issues, or any consequences for your TikTok or other accounts. The app uses overlays and an accessibility service. Nothing here is a guarantee against screen burn-in. This project is not affiliated with TikTok, ByteDance, Google or Samsung.

### Privacy note
The app still contains the upstream Firebase configuration. This build has analytics deactivated, but crash reporting from the upstream app may be active; you can turn it off with the Google/Crashlytics switch in *Additional settings*.

### License / free to use
Everything added in this fork (soft edges, solid black mode, workflow, notes) is released into the **public domain (CC0)**: use, copy, change and redistribute it however you like, no attribution needed. The original app and code are by [0mnr0](https://github.com/0mnr0/TikTokAntiBurn); that repository has no license file, so check with the upstream author for rights to the original code.


---

## Original README (upstream)

# [Download TikTok AntiBurn](https://github.com/0mnr0/TikTokAntiBurn/releases)
The purpose of the app is simple - to overlap the TikTok app elements to prevent the screen from burning out. <br>
<br>
### Preview of AntiBurn
<div style="display: flex">
    <img alt="Usual work" src="https://github.com/0mnr0/TikTokAntiBurn/blob/master/app/sampledata/usualWork.jpg" width="200" />
    <img alt="Burnout blocks demo" src="https://github.com/0mnr0/TikTokAntiBurn/blob/master/app/sampledata/customBlocks.jpg" width="200" />
    <img alt="LSPosed module demo" src="https://github.com/0mnr0/TikTokAntiBurn/blob/master/app/sampledata/root_present.jpg" width="200" />
</div>

### How is it works?
After granting two permissions, the application will start receiving application events, for example from closing and opening.
If the application detects that TikTok has opened, apply all the rules from the application. When closing, close all floating windows. It's simple!
<br><br>

### How does the LSPosed module work?
The module iterates through the application interface and searches for specific elements and then modifies it. It works simply, but it's hard to write :) 

<br>

> [!NOTE]
> Unfortunately, the iOS version will not be developed (at least by me) due to the lack of Mac devices and a number of iOS software limitations, such as creating "floating windows" and tracking running applications. My apologies

> [!WARNING]
> The XPosed module may not work due to TikTok's obfuscated structure. After each update, obfuscation occurs anew, which is why you have to search for "scripted" elements, as a result of which, with different interfaces, the module may not find the necessary elements.
