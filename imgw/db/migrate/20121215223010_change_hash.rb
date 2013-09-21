class ChangeHash < ActiveRecord::Migration
  change_table :images do |t|
	t.remove :hash
	t.string :hash_sha1
  end
end
