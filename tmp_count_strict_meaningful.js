const fs = require("fs");

function stripCommentsByExt(text, ext) {
  if (ext === ".java" || ext === ".fsh" || ext === ".vsh" || ext === ".glsl") {
    text = text.replace(/\/\*[\s\S]*?\*\//g, "");
    text = text.replace(/\/\/.*$/gm, "");
  }
  return text;
}

const files = fs
  .readFileSync(0, "utf8")
  .split(/\r?\n/)
  .map((s) => s.trim())
  .filter(Boolean);

let total = 0;
for (const file of files) {
  const ext = file.slice(file.lastIndexOf(".")).toLowerCase();
  let text = fs.readFileSync(file, "utf8");
  text = stripCommentsByExt(text, ext);
  total += text.split(/\r?\n/).filter((line) => line.trim().length > 0).length;
}

console.log(total);
