# Cashout Counter R8 rules.
# Compose and AndroidX ship consumer rules, so only app-specific rules are needed here.

# Keep source file names and line numbers for readable crash reports
-keepattributes SourceFile,LineNumberTable

# The calculator logic uses only JDK classes — nothing to keep.
# Reflection is not used anywhere in this app.
