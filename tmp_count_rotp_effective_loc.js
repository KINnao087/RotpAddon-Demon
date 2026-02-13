const fs = require("fs");
const cp = require("child_process");

const jarPath = "E:\\mc_mod\\RotP-Addon-example-master\\libs\\JJBA-RipplesOfThePast-1.16.5-0.2.2.2.jar";
const javapPath = "C:\\Progra~1\\Microsoft\\jdk-17.0.7.7-hotspot\\bin\\javap.exe";
const entriesPath = "tmp_rotp_jar_entries.txt";

const entries = fs.readFileSync(entriesPath, "utf8").split(/\r?\n/).filter(Boolean);
const classes = entries
  .filter((x) => x.endsWith(".class"))
  .map((x) => x.replace(/\//g, ".").replace(/\.class$/, ""))
  .filter((x) => x.length > 0);

const lineMap = new Map();
let failed = 0;
const batchSize = 120;

for (let i = 0; i < classes.length; i += batchSize) {
  const batch = classes.slice(i, i + batchSize);
  try {
    const out = cp.execFileSync(javapPath, ["-classpath", jarPath, "-l", ...batch], {
      encoding: "utf8",
      maxBuffer: 200 * 1024 * 1024,
      stdio: ["ignore", "pipe", "pipe"],
    });

    let currentClass = null;
    const lines = out.split(/\r?\n/);
    for (const line of lines) {
      const classMatch = line.match(/\b(class|interface|enum)\s+([^\s<{]+)/);
      if (classMatch) {
        currentClass = classMatch[2];
        if (!lineMap.has(currentClass)) {
          lineMap.set(currentClass, new Set());
        }
      }

      const lnt = line.match(/\bline\s+(\d+):/);
      if (lnt && currentClass) {
        lineMap.get(currentClass).add(Number(lnt[1]));
      }
    }
  } catch (_e) {
    failed += batch.length;
  }
}

let total = 0;
for (const set of lineMap.values()) {
  total += set.size;
}

console.log("classes=" + classes.length);
console.log("failed=" + failed);
console.log("classes_with_lnt=" + lineMap.size);
console.log("effective_loc_from_lnt=" + total);
